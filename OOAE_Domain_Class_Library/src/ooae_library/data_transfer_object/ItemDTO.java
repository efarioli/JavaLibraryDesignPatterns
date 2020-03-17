package ooae_library.data_transfer_object;

import java.io.Serializable;
import java.util.Objects;

/**
 *
 * @author gdm1
 */
public class ItemDTO implements Serializable
{
    private final String description;
    private final int itemId;
    private final String name;
    private final double price;
    private final int quantityInStock;
    private final int stockReorderLevel;
    private final SupplierDTO supplier;

    public ItemDTO(
            String description, 
            int itemId, 
            String name, 
            double price, 
            int quantityInStock, 
            int stockReorderLevel, 
            SupplierDTO supplier)
    {
        this.description = description;
        this.itemId = itemId;
        this.name = name;
        this.price = price;
        this.quantityInStock = quantityInStock;
        this.stockReorderLevel = stockReorderLevel;
        this.supplier = supplier;
    }

    @Override
    public boolean equals(Object obj)
    {
        if (this == obj)
        {
            return true;
        }
        if (obj == null)
        {
            return false;
        }
        if (getClass() != obj.getClass())
        {
            return false;
        }
        final ItemDTO other = (ItemDTO) obj;
        if (this.itemId != other.itemId)
        {
            return false;
        }
        if (Double.doubleToLongBits(this.price) != Double.doubleToLongBits(other.price))
        {
            return false;
        }
        if (this.quantityInStock != other.quantityInStock)
        {
            return false;
        }
        if (this.stockReorderLevel != other.stockReorderLevel)
        {
            return false;
        }
        if (!Objects.equals(this.description, other.description))
        {
            return false;
        }
        if (!Objects.equals(this.name, other.name))
        {
            return false;
        }
        if (!Objects.equals(this.supplier, other.supplier))
        {
            return false;
        }
        return true;
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

    public SupplierDTO getSupplier()
    {
        return supplier;
    }

    @Override
    public int hashCode()
    {
        int hash = 7;
        hash = 83 * hash + Objects.hashCode(this.description);
        hash = 83 * hash + this.itemId;
        hash = 83 * hash + Objects.hashCode(this.name);
        hash = 83 * hash + (int) (Double.doubleToLongBits(this.price) ^ (Double.doubleToLongBits(this.price) >>> 32));
        hash = 83 * hash + this.quantityInStock;
        hash = 83 * hash + this.stockReorderLevel;
        hash = 83 * hash + Objects.hashCode(this.supplier);
        return hash;
    }
}
