package dal;


import com.microsoft.sqlserver.jdbc.SQLServerDataSource;
import com.microsoft.sqlserver.jdbc.SQLServerException;

import java.sql.Connection;


/**
 * Manages the database connections for the application.
 * Utilizes SQLServerDataSource to establish connections to the SQL Server database.
 */
public class ConnectionManager {
    private final SQLServerDataSource ds;

    /**
     * Constructor for ConnectionManager. Initializes the SQLServerDataSource with database connection details.
     */
    public ConnectionManager() {
        ds = new SQLServerDataSource();

        // Sets the database name, user, password, and server name for the data source.
        ds.setDatabaseName("MoviePlayer24");
        ds.setUser("CSe2023b_e_5");
        ds.setPassword("CSe2023bE5#23 ");
        ds.setServerName("EASV-DB4");

        // Trusts the SQL Server certificate for establishing the connection.
        ds.setTrustServerCertificate(true);
    }

    /**
     * Provides a connection to the database configured in the SQLServerDataSource.
     *
     * @return A connection to the database.
     * @throws SQLServerException If a database access error occurs.
     */
    public Connection getConnection() throws SQLServerException {
        // Returns a new connection to the database.
        return ds.getConnection();
    }
}