
package co.timecrypt.api.v2.database.postgresql;

import co.timecrypt.api.v2.database.TimecryptDataStore;
import co.timecrypt.api.v2.exceptions.InvalidIdentifierException;
import co.timecrypt.api.v2.servlets.TimecryptApiServlet;
import co.timecrypt.utils.TextUtils;

import java.sql.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

/**
 * This class does all of the database work. You should put your implementation here if you want to change something.
 */
public class SimplePostgresDatabase implements TimecryptDataStore {

    private String uri;
    private String user;
    private String pass;

    @Override
    public void init(TimecryptApiServlet creator) throws IllegalStateException {
        String host;
        String port;

        try (Scanner scanner = new Scanner(creator.getServletContext().getResourceAsStream("/local.properties"))) {
            Map<String, String> config = new HashMap<>();

            // loop through all rows to find the values
            String[] splitRow;
            while (scanner.hasNext()) {
                splitRow = scanner.nextLine().split("=");
                config.put(splitRow[0], splitRow[1]);
            }

            host = config.get(PostgresConfig.PROP_HOST);
            port = config.get(PostgresConfig.PROP_PORT);
            user = config.get(PostgresConfig.PROP_USER);
            pass = config.get(PostgresConfig.PROP_PASS);
        } catch (Exception e) {
            creator.log("Did you put your 'local.properties' at 'webapp' root?", e);

            // now try the environment variable configuration (server build)
            Map<String, String> variables = System.getenv();
            host = variables.get(PostgresConfig.ENV_HOST);
            port = variables.get(PostgresConfig.ENV_PORT);
            user = variables.get(PostgresConfig.ENV_USER);
            pass = variables.get(PostgresConfig.ENV_PASS);
        }

        if (TextUtils.isAnyEmpty(host, port, user, pass)) {
            throw new IllegalStateException("Host, port, username and password must be defined in configuration");
        }

        uri = String.format("jdbc:postgresql://%s:%s/%s", host, port, PostgresConfig.DATABASE);

        // test for library dependency 
        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("PostgreSQL driver not found", e);
        }
    }

    @Override
    public boolean checkLock(String id) throws InvalidIdentifierException {
        Connection connection = open();
        PreparedStatement statement = null;
        ResultSet results = null;

        try {
            long dataId = Integer.parseInt(id, 36);

            connection.setAutoCommit(true);
            statement = connection.prepareStatement("SELECT message.locked FROM message WHERE message.id = ?");
            statement.setLong(1, dataId);
            results = statement.executeQuery();

            if (results.next()) {
                return results.getBoolean("locked"); // 0 because it will be the only column at index [0]
            } else {
                throw new InvalidIdentifierException("No results for " + id);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new InvalidIdentifierException("SQL exception: " + e.getMessage());
        } catch (NumberFormatException e) {
            e.printStackTrace();
            throw new InvalidIdentifierException("Number exception: " + e.getMessage());
        } finally {
            close(results);
            close(statement);
            close(connection);
        }
    }

    @Override
    public String create(int viewCount, String destructDate, String email, String text, String title, String password) {
        /*
        try (
        Connection connection = dataSource.getConnection();
        PreparedStatement statement = connection.prepareStatement(SQL_INSERT,
                                      Statement.RETURN_GENERATED_KEYS);
        ) {
        statement.setString(1, user.getName());
        statement.setString(2, user.getPassword());
        statement.setString(3, user.getEmail());
        // ...
        
        int affectedRows = statement.executeUpdate();
        
        if (affectedRows == 0) {
            throw new SQLException("Creating user failed, no rows affected.");
        }
        
        try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
            if (generatedKeys.next()) {
                user.setId(generatedKeys.getLong(1));
            }
            else {
                throw new SQLException("Creating user failed, no ID obtained.");
            }
        }
        }
        * */
        return "";
    }

    private Connection open() {
        try {
            return DriverManager.getConnection(uri, user, pass);
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
        // do nothing here?
    }

}
