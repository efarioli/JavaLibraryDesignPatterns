package ooae_server.entity;

import java.util.ArrayList;
import ooae_library.data_transfer_object.ItemDTO;
import ooae_server.database.ItemGateway;

/**
 *
 * @author gdm1
 */
public class Item
{

    private final String description;
    private final int itemId;
    private final String name;
    private final double price;
    private final int quantityInStock;
    private final int stockReorderLevel;
    private final Supplier supplier;

    public Item(ItemDTO item)
    {
        this.description = item.getDescription();
        this.itemId = item.getItemId();
        this.name = item.getName();
        this.price = item.getPrice();
        this.quantityInStock = item.getQuantityInStock();
        this.stockReorderLevel = item.getStockReorderLevel();
        this.supplier = new Supplier(item.getSupplier());
    }

    public Item(
            String description, 
            int itemId, 
            String name, 
            double price, 
            int quantityInStock, 
            int stockReorderLevel, 
            Supplier supplier)
    {
        this.description = description;
        this.itemId = itemId;
        this.name = name;
        this.price = price;
        this.quantityInStock = quantityInStock;
        this.stockReorderLevel = stockReorderLevel;
        this.supplier = supplier;
    }

    public static ArrayList<Item> findAllItems() throws Exception
    {
        ItemGateway itemTable = new ItemGateway();

        return itemTable.getAllItems();
    }

    public static Item findItem(int itemId) throws Exception
    {
        ItemGateway itemTable = new ItemGateway();

        return itemTable.findItem(itemId);
    }

    public static ArrayList<Item> findItemsToReorder() throws Exception
    {
        ItemGateway itemTable = new ItemGateway();

        return itemTable.getItemsToReorder();
    }

    public String getDescription()
    {
        return description;
    }

    public int getItemId()
    {
        return itemId;
    }

    public String getName()
    {
        return name;
    }

    public double getPrice()
    {
        return price;
    }

    public int getQuantityInStock()
    {
        return quantityInStock;
    }

    public int getStockReorderLevel()
    {
        return stockReorderLevel;
    }

    public Supplier getSupplier()
    {
        return supplier;
    }

    public Item insert() throws Exception
    {
        ItemGateway itemTable = new ItemGateway();

        return itemTable.insertItem(this);
    }

    public Item updateItemQuantityInStock() throws Exception
    {
        ItemGateway itemTable = new ItemGateway();

        return itemTable.updateItemQuantityInStock(this);
    }
}
