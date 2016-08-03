
package co.timecrypt.api.v2.database;

import co.timecrypt.api.v2.servlets.TimecryptApiServlet;
import co.timecrypt.api.v2.utils.Config;
import co.timecrypt.utils.TextUtils;
import com.sun.media.sound.InvalidDataException;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
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

        uri = String.format("jdbc:postgresql://%s:%s/%s", host, port, Config.DB_TABLE_NAME);

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



        close(connection);

        return false;
    }

    private Connection open() {
        try {
            return DriverManager.getConnection(uri, user, pass);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Something is really wrong", e);
        }
    }

    private void close(Connection connection) {
        if (connection != null) {
            try {
                connection.close();
            } catch (SQLException ignored) {
                // I don't really care
            }
        }
    }

    @Override
    public void destroy() {
        // TODO do nothing here?
    }

}
