package ooae_library.data_transfer_object;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Objects;

/**
 *
 * @author gdm1
 */
public class SupplierDTO implements Serializable
{
    private HashMap<Integer, ItemDTO> items;
    private final String name;
    private final int supplierId;

    public SupplierDTO(String name, int supplierId)
    {
        this.name = name;
        this.supplierId = supplierId;
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
        final SupplierDTO other = (SupplierDTO) obj;
        if (this.supplierId != other.supplierId)
        {
            return false;
        }
        if (!Objects.equals(this.name, other.name))
        {
            return false;
        }
        return true;
    }

    public HashMap<Integer, ItemDTO> getItems()
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

    public void setItems(HashMap<Integer, ItemDTO> items)
    {
        this.items = items;
    }

    @Override
    public int hashCode()
    {
        int hash = 7;
        hash = 67 * hash + Objects.hashCode(this.name);
        hash = 67 * hash + this.supplierId;
        return hash;
    }
}
