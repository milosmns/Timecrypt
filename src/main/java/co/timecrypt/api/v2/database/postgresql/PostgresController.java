package co.timecrypt.api.v2.database.postgresql;

import co.timecrypt.api.v2.database.TimecryptDataStore;
import co.timecrypt.api.v2.email.EmailConfig;
import co.timecrypt.api.v2.email.EmailSender;
import co.timecrypt.api.v2.encryption.SimpleAES;
import co.timecrypt.api.v2.exceptions.*;
import co.timecrypt.api.v2.servlets.TimecryptApiServlet;
import co.timecrypt.utils.TextUtils;

import java.sql.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * This class does all of the database work. You should put your implementation here if you want to change something.
 */
public class PostgresController implements TimecryptDataStore {

    private String dbUri;
    private String dbUser;
    private String dbPass;
    private String defaultPassword;
    private String emailKey;
    private String inviteTemplate;
    private String notifyTemplate;

    @Override
    public void init(TimecryptApiServlet creator) throws IllegalStateException {
        String host;
        String port;

        // scan local.properties and environment variables for configuration values
        try (Scanner propertiesScanner = new Scanner(creator.getServletContext().getResourceAsStream("/local.properties"))) {
            Map<String, String> config = new HashMap<>();

            // loop through all rows to find the configuration
            String[] splitRow;
            while (propertiesScanner.hasNextLine()) {
                splitRow = propertiesScanner.nextLine().split("=");
                config.put(splitRow[0], splitRow[1]);
            }

            // save the values
            host = config.get(PostgresConfig.PROP_HOST);
            port = config.get(PostgresConfig.PROP_PORT);
            dbUser = config.get(PostgresConfig.PROP_USER);
            dbPass = config.get(PostgresConfig.PROP_PASS);
            defaultPassword = config.get(PostgresConfig.PROP_DEFAULT_PASS);
            emailKey = config.get(EmailConfig.PROP_MAIL);
        } catch (Exception e) {
            creator.log("Did you put your 'local.properties' at 'webapp' root?", e);

            // no local.properties, try the environment variable configuration (server build)
            Map<String, String> variables = System.getenv();
            host = variables.get(PostgresConfig.ENV_HOST);
            port = variables.get(PostgresConfig.ENV_PORT);
            dbUser = variables.get(PostgresConfig.ENV_USER);
            dbPass = variables.get(PostgresConfig.ENV_PASS);
            defaultPassword = variables.get(PostgresConfig.ENV_DEFAULT_PASS);
            emailKey = variables.get(EmailConfig.ENV_HOST);
        }

        // now scan and pre-load email templates
        try (Scanner inviteScanner = new Scanner(creator.getServletContext().getResourceAsStream("/email-invitation-template.html"));
             Scanner notifyScanner = new Scanner(creator.getServletContext().getResourceAsStream("/email-notification-template.html"))) {
            // load the invite template
            StringBuilder templateBuilder = new StringBuilder();
            while (inviteScanner.hasNextLine()) {
                templateBuilder.append(inviteScanner.nextLine());
            }
            inviteTemplate = templateBuilder.toString();

            // load the notification template
            templateBuilder = new StringBuilder();
            while (notifyScanner.hasNextLine()) {
                templateBuilder.append(notifyScanner.nextLine());
            }
            notifyTemplate = templateBuilder.toString();
        } catch (Exception e) {
            creator.log("Cannot find email templates. Did you put them in 'webapp' root?", e);
        }

        // check if everything loaded properly, or load the URI
        if (TextUtils.isAnyEmpty(host, port, dbUser, dbPass, defaultPassword, emailKey, inviteTemplate, notifyTemplate)) {
            throw new IllegalStateException("Secret info must be defined in configuration file 'local.properties' at 'webapp' root");
        }
        dbUri = String.format("jdbc:postgresql://%s:%s/%s", host, port, PostgresConfig.DATABASE);

        // test PostgreSQL driver dependency
        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("PostgreSQL driver not found", e);
        }
    }

    @Override
    public boolean checkLock(String id) throws InvalidIdentifierException, InternalException {
        Connection connection = open();
        PreparedStatement statement = null;
        ResultSet results = null;

        try {
            // all IDs are in base_36 (all alphanumeric characters)
            long dataId = Integer.parseInt(id, 36);

            connection.setAutoCommit(true);
            statement = connection.prepareStatement("SELECT message.locked FROM message WHERE message.id = ?");
            statement.setLong(1, dataId);
            results = statement.executeQuery();

            if (results.next()) {
                return results.getBoolean("locked");
            } else {
                throw new InvalidIdentifierException("No results for " + id);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new InternalException("SQL error: " + e.getMessage());
        } catch (NumberFormatException e) {
            e.printStackTrace();
            throw new InvalidIdentifierException("Number problem: " + e.getMessage());
        } finally {
            close(results);
            close(statement);
            close(connection);
        }
    }

    @Override
    public String create(String viewCount, String destructDate, String emailTo, String emailFrom, String text, String title, String password)
            throws InvalidViewCountException, InvalidDestructDateException, InvalidEmailException, InvalidTextException, InvalidTitleException, InvalidPasswordException, InternalException {
        // validate view count (must be greater than 0 and a valid number, or null)
        int views = 1;
        if (viewCount != null) {
            try {
                views = Integer.parseInt(viewCount);
                if (views < 1) {
                    throw new NumberFormatException("Number must be greater than 0");
                }
            } catch (NumberFormatException e) {
                e.printStackTrace();
                throw new InvalidViewCountException("Cannot use viewCount parameter: " + e.getMessage());
            }
        }

        // validate destruct date (default is tomorrow)
        Calendar tomorrowCal = Calendar.getInstance();
        tomorrowCal.setTime(new Date(System.currentTimeMillis()));
        tomorrowCal.set(Calendar.HOUR_OF_DAY, 0);
        tomorrowCal.set(Calendar.MINUTE, 0);
        tomorrowCal.set(Calendar.SECOND, 0);
        tomorrowCal.add(Calendar.DATE, 1);
        java.sql.Date endOfLife = new java.sql.Date(tomorrowCal.getTime().getTime()); // such a strange API...
        if (destructDate != null) {
            try {
                Calendar requestedCal = Calendar.getInstance();
                // noinspection deprecation - warning, go away.
                SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
                Date requestedDate = formatter.parse(destructDate);
                requestedCal.setTime(requestedDate);
                // check if requested time makes sense (must be at least one day in the future)
                if (requestedCal.compareTo(tomorrowCal) >= 0) {
                    endOfLife = new java.sql.Date(requestedCal.getTime().getTime()); // this API...
                } else {
                    throw new IllegalArgumentException("Date is out of bounds " + destructDate);
                }
            } catch (IllegalArgumentException | ParseException e) {
                e.printStackTrace();
                throw new InvalidDestructDateException("Date problem: " + e.getMessage());
            }
        }

        // validate emails
        String regex = "^[a-zA-Z0-9_!#$%&'*+/=?`{|}~^.-]+@[a-zA-Z0-9.-]+$";
        Pattern pattern = Pattern.compile(regex);
        if (emailTo != null) {
            Matcher matcher = pattern.matcher(emailTo);
            if (!matcher.matches()) {
                throw new InvalidEmailException(InvalidEmailException.Type.TO, "Invalid email to " + emailTo);
            }
        }
        String notificationEmail = null;
        if (emailFrom != null) {
            Matcher matcher = pattern.matcher(emailFrom);
            if (!matcher.matches()) {
                throw new InvalidEmailException(InvalidEmailException.Type.FROM, "Invalid email from " + emailFrom);
            }
            notificationEmail = SimpleAES.encrypt(emailFrom, defaultPassword);
        }

        // update the 'locked' property and verify password
        boolean isLocked = password != null;
        String passphrase = isLocked ? password : defaultPassword;
        if (TextUtils.isEmpty(passphrase)) {
            throw new InvalidPasswordException("Cannot use an empty password. Default password is also empty");
        }

        // simple text validation
        String message;
        if (text.length() > PostgresConfig.MESSAGE_MAX_LENGTH) {
            throw new InvalidTextException("Message is more than " + PostgresConfig.MESSAGE_MAX_LENGTH + " chars long");
        }
        message = SimpleAES.encrypt(text, passphrase);

        // length title validation (must be at least MAX_LENGTH chars long)
        String secretTitle = title;
        if (title != null) {
            if (title.length() > PostgresConfig.TITLE_MAX_LENGTH) {
                throw new InvalidTitleException("Title is more than " + PostgresConfig.TITLE_MAX_LENGTH + " chars long");
            }
            secretTitle = SimpleAES.encrypt(title, passphrase);
        }

        // finally add the data to the DB
        Connection connection = open();
        PreparedStatement statement = null;
        ResultSet results = null;

        try {
            connection.setAutoCommit(false);
            statement = connection.prepareStatement("INSERT INTO message (id, view_times, lifetime, email, text, title, locked)" +
                    "VALUES (DEFAULT, ?, ?, ?, ?, ?, ?)", Statement.RETURN_GENERATED_KEYS);
            statement.setInt(1, views);
            statement.setDate(2, endOfLife);
            statement.setString(3, notificationEmail);
            statement.setString(4, message);
            statement.setString(5, secretTitle);
            statement.setBoolean(6, isLocked);

            int affectedRows = statement.executeUpdate();
            if (affectedRows == 0) {
                connection.rollback();
                throw new InternalException("Creating message failed, no rows affected");
            }

            results = statement.getGeneratedKeys();
            if (results.next()) {
                // created - finalize the transaction, read message ID, send invite if needed
                connection.commit();
                long realId = results.getLong(1); // '1' is the only column here

                // convert to base 36 for readability
                String textId = Long.toString(realId, 36);
                String textDate = endOfLife.toString();

                // try to send the email
                if (emailTo != null) {
                    EmailSender sender = new EmailSender(emailKey);
                    String invitation = sender.prepare(inviteTemplate, textId, textDate, String.valueOf(views));
                    boolean sent = sender.send(emailTo, invitation, true);
                    System.out.println("Email sending to " + emailTo + " successful? " + sent);
                }

                return textId;
            } else {
                connection.rollback();
                throw new InternalException("Could not move to next result row");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new InternalException("SQL error: " + e.getMessage());
        } finally {
            close(results);
            close(statement);
            close(connection);
        }
    }

    private Connection open() {
        try {
            return DriverManager.getConnection(dbUri, dbUser, dbPass);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Something is really wrong", e);
        }
    }

    private void close(AutoCloseable autoCloseable) {
        if (autoCloseable != null) {
            try {
                autoCloseable.close();
            } catch (Exception ignored) {
                // I don't really care
            }
        }
    }

    @Override
    public void destroy() {
        // TODO: do nothing here?
    }

}
