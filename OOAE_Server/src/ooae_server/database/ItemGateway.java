package ooae_server.database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import ooae_server.entity.Item;
import ooae_server.entity.Supplier;

/**
 *
 * @author gdm1
 */
public class ItemGateway extends DB_ConnectionManager
{

    private static final String CREATE_ITEM_TABLE
            = "CREATE TABLE Item "
            + "("
            + "ItemId BIGINT NOT NULL "
            + "GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1) "
            + "CONSTRAINT Item_PK PRIMARY KEY, "
            + "Name VARCHAR(20) NOT NULL, "
            + "Description VARCHAR(100) NOT NULL, "
            + "SupplierId BIGINT NOT NULL "
            + "CONSTRAINT SupplierId_FK REFERENCES Supplier(SupplierId), "
            + "Price DECIMAL(10, 2) NOT NULL, "
            + "QuantityInStock INTEGER NOT NULL, "
            + "StockReorderLevel INTEGER NOT NULL "
            + ")";
    private static final String DECREMENT_ITEM_QTY = "UPDATE Item SET QuantityInStock = QuantityInStock - ? WHERE ItemId = ?";
    private static final String DROP_ITEM_TABLE = "DROP TABLE Item";
    private static final String GET_ALL_ITEMS
            = "SELECT Item.ItemId, Item.Name, Item.Description, Item.Price, Item.QuantityInStock, item.StockReorderLevel, Supplier.SupplierId, Supplier.Name AS SupplierName "
            + "FROM Item JOIN Supplier ON Item.SupplierId = Supplier.SupplierId";
    private static final String GET_ITEM_BY_DETAILS
            = "SELECT Item.ItemId, Item.Name, Item.Description, Item.Price, Item.QuantityInStock, item.StockReorderLevel, Supplier.SupplierId, Supplier.Name AS SupplierName "
            + "FROM Item JOIN Supplier ON Item.SupplierId = Supplier.SupplierId "
            + "WHERE Item.Name = ? AND Item.Description = ? AND Item.Price = ? AND Item.QuantityInStock = ? AND item.StockReorderLevel = ? AND Supplier.SupplierId = ?";
    private static final String GET_ITEM_BY_ID
            = "SELECT Item.ItemId, Item.Name, Item.Description, Item.Price, Item.QuantityInStock, item.StockReorderLevel, Supplier.SupplierId, Supplier.Name AS SupplierName "
            + "FROM Item JOIN Supplier ON Item.SupplierId = Supplier.SupplierId "
            + "WHERE Item.ItemId = ?";
    private static final String GET_ITEMS_TO_REORDER
            = "SELECT Item.ItemId, Item.Name, Item.Description, Item.Price, Item.QuantityInStock, item.StockReorderLevel, Supplier.SupplierId, Supplier.Name AS SupplierName "
            + "FROM Item JOIN Supplier ON Item.SupplierId = Supplier.SupplierId "
            + "WHERE Item.QuantityInStock <= Item.StockReorderLevel";
    private static final String INSERT_ITEM = "INSERT INTO Item (Name, Description, SupplierId, Price, QuantityInStock, StockReorderLevel) VALUES (?, ?, ?, ?, ?, ?)";
    private static final String UPDATE_ITEM_QTY = "UPDATE Item SET QuantityInStock = ? WHERE ItemId = ?";

    public void decrementItemQuantityInStock(int itemId, int amount) throws Exception
    {
        Connection conn = getConnection();

        try
        {
            PreparedStatement stmt = conn.prepareStatement(DECREMENT_ITEM_QTY);
            stmt.setInt(1, amount);
            stmt.setInt(2, itemId);
            stmt.executeUpdate();
            stmt.close();
        } catch (SQLException sqle)
        {
            throw new Exception("ERROR: Decrement of item quantity failed", sqle);
        }

        closeConnection(conn);
    }

    @Override
    protected void doDropTable(Connection conn)
    {
        try
        {
            PreparedStatement stmt = conn.prepareStatement(DROP_ITEM_TABLE);
            stmt.executeUpdate();
            stmt.close();
        } catch (SQLException sqle)
        {
        }
    }

    @Override
    protected boolean doExists(Connection conn)
    {
        try
        {
            PreparedStatement stmt = conn.prepareStatement(GET_ALL_ITEMS + " FETCH FIRST 1 ROWS ONLY");
            stmt.executeQuery().close();
            stmt.close();
            return true;

        } catch (SQLException ex)
        {
            return false;

        }
    }

    private Item findItem(String name, String description, int supplierId, double price, int quantityInStock, int stockReorderLevel) throws Exception
    {
        Item item = null;

        Connection conn = getConnection();

        try
        {
            PreparedStatement stmt = conn.prepareStatement(GET_ITEM_BY_DETAILS);
            stmt.setString(1, name);
            stmt.setString(2, description);
            stmt.setDouble(3, price);
            stmt.setDouble(4, quantityInStock);
            stmt.setDouble(5, stockReorderLevel);
            stmt.setInt(6, supplierId);
            ResultSet rs = stmt.executeQuery();

            if (rs.next())
            {
                Supplier supplier = new Supplier(
                        rs.getString("SupplierName"),
                        rs.getInt("SupplierId"));
                supplier.setItems(null);

                item = new Item(
                        rs.getString("Description"),
                        rs.getInt("ItemId"),
                        rs.getString("Name"),
                        rs.getDouble("Price"),
                        rs.getInt("QuantityInStock"),
                        rs.getInt("StockReorderLevel"),
                        supplier);
            }

            rs.close();
            stmt.close();
        } catch (SQLException sqle)
        {
            throw new Exception("ERROR: retrieval of item failed", sqle);
        }

        closeConnection(conn);
        return item;
    }

    public Item findItem(int itemId) throws Exception
    {
        Item item = null;

        Connection conn = getConnection();

        try
        {
            PreparedStatement stmt = conn.prepareStatement(GET_ITEM_BY_ID);
            stmt.setInt(1, itemId);
            ResultSet rs = stmt.executeQuery();

            if (rs.next())
            {
                Supplier supplier = new Supplier(
                        rs.getString("SupplierName"),
                        rs.getInt("SupplierId"));
                supplier.setItems(null);

                item = new Item(
                        rs.getString("Description"),
                        rs.getInt("ItemId"),
                        rs.getString("Name"),
                        rs.getDouble("Price"),
                        rs.getInt("QuantityInStock"),
                        rs.getInt("StockReorderLevel"),
                        supplier);
            }

            rs.close();
            stmt.close();
        } catch (SQLException sqle)
        {
            throw new Exception("ERROR: retrieval of item failed", sqle);
        }

        closeConnection(conn);
        return item;
    }

    public ArrayList<Item> getAllItems() throws Exception
    {
        ArrayList<Item> items = new ArrayList<>();

        Connection conn = getConnection();

        try
        {
            PreparedStatement stmt = conn.prepareStatement(GET_ALL_ITEMS);
            ResultSet rs = stmt.executeQuery();

            while (rs.next())
            {
                Supplier supplier = new Supplier(
                        rs.getString("SupplierName"),
                        rs.getInt("SupplierId"));
                supplier.setItems(null);

                Item item = new Item(
                        rs.getString("Description"),
                        rs.getInt("ItemId"),
                        rs.getString("Name"),
                        rs.getDouble("Price"),
                        rs.getInt("QuantityInStock"),
                        rs.getInt("StockReorderLevel"),
                        supplier);

                items.add(item);
            }

            rs.close();
            stmt.close();
        } catch (SQLException sqle)
        {
            throw new Exception("ERROR: retrieval of items failed", sqle);
        }

        closeConnection(conn);

        return items;
    }

    public ArrayList<Item> getItemsToReorder() throws Exception
    {
        ArrayList<Item> items = new ArrayList<>();

        Connection conn = getConnection();

        try
        {
            PreparedStatement stmt = conn.prepareStatement(GET_ITEMS_TO_REORDER);
            ResultSet rs = stmt.executeQuery();

            while (rs.next())
            {
                Supplier supplier = new Supplier(
                        rs.getString("SupplierName"),
                        rs.getInt("SupplierId"));
                supplier.setItems(null);

                Item item = new Item(
                        rs.getString("Description"),
                        rs.getInt("ItemId"),
                        rs.getString("Name"),
                        rs.getDouble("Price"),
                        rs.getInt("QuantityInStock"),
                        rs.getInt("StockReorderLevel"),
                        supplier);

                items.add(item);
            }

            rs.close();
            stmt.close();
        } catch (SQLException sqle)
        {
            throw new Exception("ERROR: retrieval of items failed", sqle);
        }

        closeConnection(conn);

        return items;
    }

    public void initialiseTable() throws Exception
    {
        Connection conn = getConnection();

        try
        {
            PreparedStatement stmt = conn.prepareStatement(CREATE_ITEM_TABLE);
            stmt.executeUpdate();
            stmt.close();
        } catch (SQLException sqle)
        {
            throw new Exception("ERROR: Item table not created", sqle);
        }

        try
        {
            PreparedStatement stmt = conn.prepareStatement(INSERT_ITEM);

            for (int i = 1; i < 11; i++)
            {
                stmt.setString(1, "Item " + i);
                stmt.setString(2, "Description of item " + i);
                stmt.setInt(3, i % 5 + 1);
                stmt.setDouble(4, Double.valueOf(i + "." + i + "5"));
                stmt.setInt(5, i % 5 + 4);
                stmt.setInt(6, 5);
                stmt.executeUpdate();
            }
            stmt.close();
        } catch (SQLException sqle)
        {
            throw new Exception("ERROR: Item table not created", sqle);
        }

        closeConnection(conn);
    }

    public Item insertItem(Item item) throws Exception
    {
        Item insertedItem = null;

        Connection conn = getConnection();

        try
        {
            PreparedStatement stmt = conn.prepareStatement(INSERT_ITEM);
            stmt.setString(1, item.getName());
            stmt.setString(2, item.getDescription());
            stmt.setInt(3, item.getSupplier().getSupplierId());
            stmt.setDouble(4, item.getPrice());
            stmt.setInt(5, item.getQuantityInStock());
            stmt.setInt(6, item.getStockReorderLevel());
            stmt.executeUpdate();
            stmt.close();

            insertedItem = findItem(item.getName(), item.getDescription(), item.getSupplier().getSupplierId(), item.getPrice(), item.getQuantityInStock(), item.getStockReorderLevel());
        } catch (SQLException sqle)
        {
            throw new Exception("ERROR: Item insertion failed", sqle);
        }

        closeConnection(conn);
        return insertedItem;
    }

    public Item updateItemQuantityInStock(Item item) throws Exception
    {
        Item updatedItem = null;

        Connection conn = getConnection();

        try
        {
            PreparedStatement stmt = conn.prepareStatement(UPDATE_ITEM_QTY);
            stmt.setInt(1, item.getQuantityInStock());
            stmt.setInt(2, item.getItemId());
            stmt.executeUpdate();
            stmt.close();

            updatedItem = findItem(item.getItemId());
        } catch (SQLException sqle)
        {
            throw new Exception("ERROR: Item insertion failed", sqle);
        }

        closeConnection(conn);
        return updatedItem;
    }
}
