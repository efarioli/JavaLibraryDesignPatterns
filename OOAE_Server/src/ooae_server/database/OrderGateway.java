package ooae_server.database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.HashMap;
import ooae_server.entity.Customer;
import ooae_server.entity.Item;
import ooae_server.entity.Order;
import ooae_server.entity.OrderLine;

/**
 *
 * @author gdm1
 */
public class OrderGateway extends DB_ConnectionManager
{

    private static final String CANCEL_ORDER = "UPDATE Orders SET Status = 'Cancelled' WHERE OrderId = ?";
    private static final String CREATE_ORDER_TABLE
            = "CREATE TABLE Orders "
            + "("
            + "OrderId BIGINT NOT NULL "
            + "GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1) "
            + "CONSTRAINT Order_PK PRIMARY KEY, "
            + "CustomerId BIGINT NOT NULL "
            + "CONSTRAINT CustomerId_FK REFERENCES Customer(CustomerId), "
            + "OrderDateTime TIMESTAMP NOT NULL, "
            + "Status VARCHAR(9) NOT NULL"
            + ")";
    private static final String CREATE_ORDERLINE_TABLE
            = "CREATE TABLE OrderLine "
            + "("
            + "OrderLineId BIGINT NOT NULL "
            + "GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1) "
            + "CONSTRAINT OrderLine_PK PRIMARY KEY, "
            + "OrderId BIGINT NOT NULL "
            + "CONSTRAINT OrderId_FK REFERENCES Orders(OrderId), "
            + "ItemId BIGINT NOT NULL "
            + "CONSTRAINT ItemId_FK REFERENCES Item(ItemId), "
            + "Quantity INTEGER NOT NULL, "
            + "Price DECIMAL(10, 2) NOT NULL "
            + ")";
    private static final String DROP_ORDER_TABLE = "DROP TABLE Orders";
    private static final String DROP_ORDERLINE_TABLE = "DROP TABLE OrderLine";
    private static final String GET_ALL_ORDERS = "SELECT * FROM Orders";
    private static final String GET_ORDER_ID_BY_CUSTOMER_AND_TIMESTAMP = "SELECT OrderId FROM Orders WHERE CustomerId = ? AND OrderDateTime = ?";
    private static final String GET_ORDER_BY_ID
            = "SELECT Orders.*, OrderLine.OrderLineId, OrderLine.ItemId, OrderLine.Quantity, OrderLine.Price, Item.ItemId, Item.Name, Item.Description "
            + "FROM Orders JOIN OrderLine ON Orders.OrderId = OrderLine.OrderId "
            + "JOIN Item ON OrderLine.ItemId = Item.ItemId "
            + "WHERE Orders.OrderId = ?";
    private static final String GET_ORDERS_FOR_CUSTOMER
            = "SELECT Orders.*, OrderLine.OrderLineId, OrderLine.ItemId, OrderLine.Quantity, OrderLine.Price, Item.ItemId, Item.Name, Item.Description "
            + "FROM Orders JOIN OrderLine ON Orders.OrderId = OrderLine.OrderId "
            + "JOIN Item ON OrderLine.ItemId = Item.ItemId "
            + "WHERE Orders.CustomerId = ? "
            + "ORDER BY Orders.OrderId, OrderLine.OrderLineId";
    private static final String INSERT_ORDER = "INSERT INTO Orders (CustomerId, OrderDateTime, Status) VALUES (?, ?, 'Placed')";
    private static final String INSERT_ORDERLINE = "INSERT INTO OrderLine (OrderId, ItemId, Quantity, Price) VALUES (?, ?, ?, ?)";
    private static final String SHIP_ORDER = "UPDATE Orders SET Status = 'Shipped' WHERE OrderId = ?";

    public Order cancelOrder(int orderId) throws Exception
    {
        Connection conn = getConnection();

        try
        {
            PreparedStatement stmt = conn.prepareStatement(CANCEL_ORDER);
            stmt.setInt(1, orderId);
            stmt.executeUpdate();
            stmt.close();
        } catch (SQLException sqle)
        {
        }

        closeConnection(conn);

        return findOrder(orderId);
    }
    
     @Override
    protected boolean doExists(Connection conn)
    {
        try
        {
            PreparedStatement stmt = conn.prepareStatement(GET_ALL_ORDERS + " FETCH FIRST 1 ROWS ONLY");
            stmt.executeQuery().close();
            stmt.close();
            return true;

        } catch (SQLException ex)
        {
            return false;
        }
    }

    public Order findOrder(int orderId) throws Exception
    {
        return findOrder(orderId, null);
    }

    private Order findOrder(int orderId, Customer customer) throws Exception
    {
        Connection conn = getConnection();
        Order order = new Order();
        order.setCustomer(customer);

        try
        {
            PreparedStatement stmt = conn.prepareStatement(GET_ORDER_BY_ID);
            stmt.setInt(1, orderId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next())
            {
                order.setOrderId(rs.getInt("OrderId"));
                Calendar orderTimestamp = Calendar.getInstance();
                orderTimestamp.setTimeInMillis(rs.getTimestamp("OrderDateTime").getTime());
                order.setOrderDateTime(orderTimestamp);
                order.setStatus(rs.getString("Status"));

                do
                {
                    Item item = new Item(
                            rs.getString("Description"),
                            rs.getInt("ItemId"),
                            rs.getString("Name"),
                            0,
                            0,
                            0,
                            null);

                    order.addOrderLine(
                            rs.getInt("OrderLineId"),
                            item,
                            rs.getDouble("Price"),
                            rs.getInt("Quantity"));
                } while (rs.next());
            } else
            {
                throw new Exception("ERROR: There has been a problem finding the order");
            }
            stmt.close();
        } catch (SQLException sqle)
        {
//            sqle.printStackTrace();
        }

        closeConnection(conn);

        return order;
    }

    private int findOrderId(int customerId, Calendar orderDateTime) throws Exception
    {
        int newOrderId = -1;
        Connection conn = getConnection();

        try
        {
            PreparedStatement stmt = conn.prepareStatement(GET_ORDER_ID_BY_CUSTOMER_AND_TIMESTAMP);
            stmt.setInt(1, customerId);
            stmt.setTimestamp(2, new Timestamp(orderDateTime.getTimeInMillis()));
            ResultSet rs = stmt.executeQuery();
            if (rs.next())
            {
                newOrderId = rs.getInt("OrderId");
            } else
            {
                throw new Exception("ERROR: There has been a problem with the order insertion");
            }
            stmt.close();
        } catch (SQLException sqle)
        {
//            sqle.printStackTrace();
        }

        closeConnection(conn);

        return newOrderId;
    }

    public HashMap<Integer, Order> getOrdersForCustomer(Customer customer) throws Exception
    {
        HashMap<Integer, Order> orders = new HashMap<>();
        customer.setOrders(orders);

        Order order = null;
        Connection conn = getConnection();

        try
        {
            PreparedStatement stmt = conn.prepareStatement(GET_ORDERS_FOR_CUSTOMER);
            stmt.setInt(1, customer.getCustomerId());
            ResultSet rs = stmt.executeQuery();
            while (rs.next())
            {
                int orderId = rs.getInt("OrderId");

                if (order == null || order.getOrderId() != orderId)
                {
                    order = new Order();
                    order.setOrderId(orderId);
                    orders.put(order.getOrderId(), order);

                    order.setCustomer(customer);
                    order.setOrderId(orderId);
                    Calendar orderTimestamp = Calendar.getInstance();
                    orderTimestamp.setTimeInMillis(rs.getTimestamp("OrderDateTime").getTime());
                    order.setOrderDateTime(orderTimestamp);
                    order.setStatus(rs.getString("Status"));
                }

                Item item = new Item(
                        rs.getString("Description"),
                        rs.getInt("ItemId"),
                        rs.getString("Name"),
                        0,
                        0,
                        0,
                        null);

                order.addOrderLine(
                        rs.getInt("OrderLineId"),
                        item,
                        rs.getDouble("Price"),
                        rs.getInt("Quantity"));
            }
            stmt.close();
        } catch (SQLException sqle)
        {
//            sqle.printStackTrace();
        }

        closeConnection(conn);

        return orders;
    }

    public void initialiseTable() throws Exception
    {
        Connection conn = getConnection();

        try
        {
            PreparedStatement stmt = conn.prepareStatement(CREATE_ORDER_TABLE);
            stmt.executeUpdate();
            stmt.close();
        } catch (SQLException sqle)
        {
            throw new Exception("ERROR: Order table not created", sqle);
        }

        try
        {
            PreparedStatement stmt = conn.prepareStatement(CREATE_ORDERLINE_TABLE);
            stmt.executeUpdate();
            stmt.close();
        } catch (SQLException sqle)
        {
            throw new Exception("ERROR: OrderLine table not created", sqle);
        }

        try
        {
            PreparedStatement insertOrderStmt = conn.prepareStatement(INSERT_ORDER);
            PreparedStatement insertOrderLineStmt = conn.prepareStatement(INSERT_ORDERLINE);
            PreparedStatement cancelStmt = conn.prepareStatement(CANCEL_ORDER);
            PreparedStatement shipStmt = conn.prepareStatement(SHIP_ORDER);

            final int numOrders = 21;
            for (int i = 1; i < numOrders; i++)
            {
                insertOrderStmt.setInt(1, i % 10 + 1);
                Calendar cal = Calendar.getInstance();
                cal.set(Calendar.YEAR, 2019);
                cal.set(Calendar.MONTH, 7);
                cal.set(Calendar.DAY_OF_MONTH, 23);
                cal.set(Calendar.HOUR_OF_DAY, 13);
                cal.set(Calendar.MINUTE, 53);
                cal.set(Calendar.SECOND, 45);
                insertOrderStmt.setTimestamp(2, new Timestamp(cal.getTimeInMillis()));
                insertOrderStmt.executeUpdate();

                int numOrderLines = i % 6 + 1;
                for (int j = 1; j <= numOrderLines; j++)
                {
                    insertOrderLineStmt.setInt(1, i);
                    insertOrderLineStmt.setInt(2, j % 10 + 1);
                    insertOrderLineStmt.setInt(3, j);
                    insertOrderLineStmt.setDouble(4, Double.valueOf(i + "." + j + "5"));
                    insertOrderLineStmt.executeUpdate();
                }
            }
            insertOrderStmt.close();
            insertOrderLineStmt.close();

            for (int i = 3; i < numOrders; i += 3)
            {
                if (i % 6 == 0)
                {
                    cancelStmt.setInt(1, i);
                    cancelStmt.executeUpdate();
                } else
                {
                    shipStmt.setInt(1, i);
                    shipStmt.executeUpdate();
                }
            }
            cancelStmt.close();
            shipStmt.close();
        } catch (SQLException sqle)
        {
            throw new Exception("ERROR: Order status not set", sqle);
        }

        closeConnection(conn);
    }

    public Order insertOrder(Order order) throws Exception
    {
        int newOrderId = -1;
        Connection conn = getConnection();
        ItemGateway itemTable = new ItemGateway();

        try
        {
            PreparedStatement stmt = conn.prepareStatement(INSERT_ORDER);
            stmt.setInt(1, order.getCustomer().getCustomerId());
            stmt.setTimestamp(2, new Timestamp(order.getOrderDateTime().getTimeInMillis()));
            stmt.executeUpdate();
            stmt.close();

            newOrderId = findOrderId(order.getCustomer().getCustomerId(), order.getOrderDateTime());

            stmt = conn.prepareStatement(INSERT_ORDERLINE);
            stmt.setInt(1, newOrderId);

            for (OrderLine line : order.getOrderLines().values())
            {
                stmt.setInt(2, line.getItem().getItemId());
                stmt.setInt(3, line.getQuantity());
                stmt.setDouble(4, line.getPrice());
                stmt.executeUpdate();
                itemTable.decrementItemQuantityInStock(line.getItem().getItemId(), line.getQuantity());
            }
            stmt.close();
        } catch (SQLException sqle)
        {
//            sqle.printStackTrace();
        }

        closeConnection(conn);

        return findOrder(newOrderId, order.getCustomer());
    }

    public Order shipOrder(int orderId) throws Exception
    {
        Connection conn = getConnection();

        try
        {
            PreparedStatement stmt = conn.prepareStatement(SHIP_ORDER);
            stmt.setInt(1, orderId);
            stmt.executeUpdate();
            stmt.close();
        } catch (SQLException sqle)
        {
        }

        closeConnection(conn);

        return findOrder(orderId);
    }

   
    
     @Override
    protected void doDropTable(Connection conn)
    {
        try
        {
            PreparedStatement stmt = conn.prepareStatement(DROP_ORDERLINE_TABLE);
            stmt.executeUpdate();
            stmt.close();
            
            stmt = conn.prepareStatement(DROP_ORDER_TABLE);
            stmt.executeUpdate();
            stmt.close();
        } catch (SQLException ex)
        {
        }
    }
}
