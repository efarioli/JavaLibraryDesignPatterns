package ooae_server.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 *
 * @author gdm1
 */
public abstract class DB_ConnectionManager
{

    private static final String DATABASE_URL = "jdbc:derby://localhost:1527/OOAEassignment;user=ooae;password=ooae";

    protected final void closeConnection(Connection conn) throws Exception
    {
        if (conn != null)
        {
            try
            {
                conn.close();
            } catch (SQLException sqle)
            {
                throw new Exception("ERROR: closure of database connection failed", sqle);
            }
        }
    }

    protected final Connection getConnection() throws Exception
    {
        Connection conn = null;

        try
        {
            DriverManager.registerDriver(new org.apache.derby.jdbc.ClientDriver());
            conn = DriverManager.getConnection(DATABASE_URL);
        } catch (SQLException sqle)
        {
            throw new Exception("ERROR: connection to database failed", sqle);
        }

        return conn;
    }

    public void dropTable() throws Exception
    {
        Connection conn = getConnection();

        doDropTable(conn);

        closeConnection(conn);
    }

    public boolean exists() throws Exception
    {
        Connection conn = getConnection();
        boolean result = doExists(conn);
        closeConnection(conn);
        return result;
    }

    public void initialiseTable() throws Exception
    {
        Connection conn = getConnection();

        doinitialiseTable(conn);

        closeConnection(conn);
    }

    protected abstract void doDropTable(Connection conn);

    protected abstract boolean doExists(Connection conn);

    protected abstract void doinitialiseTable(Connection conn) throws Exception;

}
