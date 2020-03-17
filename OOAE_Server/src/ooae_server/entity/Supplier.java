package ooae_server.entity;

import java.util.HashMap;
import ooae_library.data_transfer_object.SupplierDTO;

/**
 *
 * @author gdm1
 */
public class Supplier
{
    private HashMap<Integer, Item> items;
    private final String name;
    private final int supplierId;

    public Supplier(SupplierDTO supplier)
    {
        this.name = supplier.getName();
        this.supplierId = supplier.getSupplierId();
    }

    public Supplier(String name, int supplierId)
    {
        this.name = name;
        this.supplierId = supplierId;
    }

    public HashMap<Integer, Item> getItems()
    {
        return items;
    }

    public String getName()
    {
        return name;
    }

    public int getSupplierId()
    {
        return supplierId;
    }

    public void setItems(HashMap<Integer, Item> items)
    {
        this.items = items;
    }
}
