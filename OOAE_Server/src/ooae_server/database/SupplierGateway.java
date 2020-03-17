package ooae_server.database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 *
 * @author gdm1
 */
public class SupplierGateway extends DB_ConnectionManager
{

    private static final String CREATE_SUPPLIER_TABLE
            = "CREATE TABLE Supplier "
            + "("
            + "SupplierId BIGINT NOT NULL "
            + "GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1) "
            + "CONSTRAINT Supplier_PK PRIMARY KEY, "
            + "Name VARCHAR(20) NOT NULL "
            + ")";
    private static final String DROP_SUPPLIER_TABLE = "DROP TABLE Supplier";
    private static final String GET_ALL_SUPPLIERS = "SELECT * FROM Supplier";
    private static final String INSERT_SUPPLIER = "INSERT INTO Supplier (Name) VALUES (?)";

    public void dropTable() throws Exception
    {
        Connection conn = getConnection();

        try
        {
            PreparedStatement stmt = conn.prepareStatement(DROP_SUPPLIER_TABLE);
            stmt.executeUpdate();
            stmt.close();
        }
        catch (SQLException sqle)
        {
        }

        closeConnection(conn);
    }

    public boolean exists()
    {
        try
        {
            Connection conn = getConnection();
            PreparedStatement stmt = conn.prepareStatement(GET_ALL_SUPPLIERS + " FETCH FIRST 1 ROWS ONLY");
            stmt.executeQuery().close();
            stmt.close();
            closeConnection(conn);
            return true;
        }
        catch (Exception e)
        {
            return false;
        }
    }

    public void initialiseTable() throws Exception
    {
        Connection conn = getConnection();

        try
        {
            PreparedStatement stmt = conn.prepareStatement(CREATE_SUPPLIER_TABLE);
            stmt.executeUpdate();
            stmt.close();
        }
        catch (SQLException sqle)
        {
            throw new Exception("ERROR: Supplier table not created", sqle);
        }

        try
        {
            PreparedStatement stmt = conn.prepareStatement(INSERT_SUPPLIER);

            for (int i = 1; i < 6; i++)
            {
                stmt.setString(1, "Supplier " + i);
                stmt.executeUpdate();
            }
            stmt.close();
        }
        catch (SQLException sqle)
        {
            throw new Exception("ERROR: Supplier table not created", sqle);
        }

        closeConnection(conn);
    }
}
