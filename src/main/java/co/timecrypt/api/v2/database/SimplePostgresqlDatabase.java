
package co.timecrypt.api.v2.database;

import co.timecrypt.api.v2.servlets.TimecryptApiServlet;
import co.timecrypt.api.v2.utils.Config;
import co.timecrypt.utils.TextUtils;
import com.sun.media.sound.InvalidDataException;

import java.sql.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

/**
 * This class does all of the database work. You should put your implementation here if you want to change something.
 */
public class SimplePostgresqlDatabase implements TimecryptDataStore {

    private String uri;
    private String host;
    private String port;
    private String user;
    private String pass;

    @Override
    public void init(TimecryptApiServlet creator) throws InvalidDataException {
        try (Scanner scanner = new Scanner(creator.getServletContext().getResourceAsStream("/local.properties"))) {
            Map<String, String> config = new HashMap<>();

            // loop through all rows to find the values
            String[] splitRow;
            while (scanner.hasNext()) {
                splitRow = scanner.nextLine().split("=");
                config.put(splitRow[0], splitRow[1]);
            }

            host = config.get("database_host");
            port = config.get("database_port");
            user = config.get("database_user");
            pass = config.get("database_pass");

            if (TextUtils.isAnyEmpty(host, port, user, pass)) {
                throw new InvalidDataException("Host, port, username and password must be defined in configuration");
            }
        } catch (Exception e) {
            creator.log("Cannot configure database. Did you put your 'local.properties' at 'webapp' root?", e);
        }

        uri = String.format("jdbc:postgresql://%s:%s/%s", host, port, Config.DATABASE_NAME);

        // test for library dependency 
        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("PostgreSQL driver not found", e);
        }
    }

    @Override
    public boolean checkLock(String id) throws InvalidDataException {
        Connection connection = open();
        PreparedStatement statement = null;
        ResultSet results = null;

        try {
            long dataId = Long.parseLong(id);

            connection.setAutoCommit(false);
            statement = connection.prepareStatement("SELECT message.locked FROM message WHERE message.id = ?");
            statement.setLong(1, dataId);
            results = statement.executeQuery();

            if (results.next()) {
                return results.getBoolean(0); // 0 because it will be the only column at index [0]
            } else {
                throw new InvalidDataException("No results for " + id);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new InvalidDataException("SQL exception: " + e.getMessage());
        } catch (NumberFormatException e) {
            e.printStackTrace();
            throw new InvalidDataException("Number exception: " + e.getMessage());
        } finally {
            close(results);
            close(statement);
            close(connection);
        }
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
        // TODO do nothing here?
    }

}
