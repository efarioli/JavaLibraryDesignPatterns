package ooae_server.database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import ooae_server.entity.Customer;

/**
 *
 * @author gdm1
 */
public class CustomerGateway extends DB_ConnectionManager
{

    private static final String CREATE_CUSTOMER_TABLE
            = "CREATE TABLE Customer "
            + "("
            + "CustomerId BIGINT NOT NULL "
            + "GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1) "
            + "CONSTRAINT Customer_PK PRIMARY KEY, "
            + "Name VARCHAR(20) NOT NULL, "
            + "UserName VARCHAR(10) NOT NULL, "
            + "Password VARCHAR(15) NOT NULL "
            + ")";
    private static final String DROP_CUSTOMER_TABLE = "DROP TABLE Customer";
    private static final String GET_ALL_CUSTOMERS = "SELECT * FROM Customer";
    private static final String GET_CUSTOMER = "SELECT * FROM Customer WHERE Username = ?";
    private static final String INSERT_CUSTOMER = "INSERT INTO Customer (Name, UserName, Password) VALUES (?, ?, ?)";

    

    @Override
    protected void doDropTable(Connection conn)
    {
        try
        {
            PreparedStatement stmt = conn.prepareStatement(DROP_CUSTOMER_TABLE);
            stmt.executeUpdate();
            stmt.close();
        } catch (SQLException sqle)
        {
        }
    }

    public boolean exists()
    {
        try
        {
            Connection conn = getConnection();
            PreparedStatement stmt = conn.prepareStatement(GET_ALL_CUSTOMERS + " FETCH FIRST 1 ROWS ONLY");
            stmt.executeQuery().close();
            stmt.close();
            closeConnection(conn);
            return true;
        } catch (Exception e)
        {
            return false;
        }
    }

    public Customer findCustomer(Customer customer) throws Exception
    {
        Customer user = new Customer();
        Connection conn = getConnection();

        try
        {
            PreparedStatement stmt = conn.prepareStatement(GET_CUSTOMER);
            stmt.setString(1, customer.getUserName());
            ResultSet rs = stmt.executeQuery();
            if (rs.next())
            {
                user.setCustomerId(rs.getInt("CustomerId"));
                user.setName(rs.getString("Name"));
                user.setUserName(rs.getString("Username"));
                user.setPassword(rs.getString("Password"));
            } else
            {
                user.setCustomerId(-1);
            }
            stmt.close();
        } catch (SQLException sqle)
        {
        }

        closeConnection(conn);

        return user;
    }

    public void initialiseTable() throws Exception
    {
        Connection conn = getConnection();

        try
        {
            PreparedStatement stmt = conn.prepareStatement(CREATE_CUSTOMER_TABLE);
            stmt.executeUpdate();
            stmt.close();
        } catch (SQLException sqle)
        {
//            sqle.printStackTrace();
            throw new Exception("ERROR: Customer table not created", sqle);
        }

        try
        {
            PreparedStatement stmt = conn.prepareStatement(INSERT_CUSTOMER);

            for (int i = 1; i < 11; i++)
            {
                stmt.setString(1, "Customer " + i);
                stmt.setString(2, "Customer" + i);
                stmt.setString(3, "Customer" + i);
                stmt.executeUpdate();
            }
            stmt.close();
        } catch (SQLException sqle)
        {
            throw new Exception("ERROR: Customer table not created", sqle);
        }

        closeConnection(conn);
    }

}
