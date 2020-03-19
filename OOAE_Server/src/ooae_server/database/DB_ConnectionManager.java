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

    private DB_ConnectionPool pool = new DB_ConnectionPool();

    protected final void closeConnection(Connection conn) throws Exception
    {
        pool.releaseConnection(conn);
    }

    protected final Connection getConnection() throws Exception
    {
        return pool.acquireConnection();
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
