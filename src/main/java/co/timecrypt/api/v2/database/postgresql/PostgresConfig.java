
package co.timecrypt.api.v2.database.postgresql;

/**
 * Basic database configuration for the PostgreSQL version.
 */
public class PostgresConfig {

    public static final String DATABASE = "timecrypt";

    public static final String PROP_HOST = "database_host";
    public static final String PROP_PORT = "database_port";
    public static final String PROP_USER = "database_user";
    public static final String PROP_PASS = "database_pass";

    public static final String ENV_HOST = "OPENSHIFT_POSTGRESQL_DB_HOST";
    public static final String ENV_PORT = "OPENSHIFT_POSTGRESQL_DB_PORT";
    public static final String ENV_USER = "OPENSHIFT_POSTGRESQL_DB_USERNAME";
    public static final String ENV_PASS = "OPENSHIFT_POSTGRESQL_DB_PASSWORD";

}
