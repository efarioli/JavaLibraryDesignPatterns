package ooae_library.data_transfer_object;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Objects;

/**
 *
 * @author gdm1
 */
public class CustomerDTO implements Serializable
{
    private final int customerId;
    private final String name;
    private HashMap<Integer, OrderDTO> orders;
    private final String password;
    private final String userName;

    public CustomerDTO(int customerId, String name, String password, String userName)
    {
        this.customerId = customerId;
        this.name = name;
        this.password = password;
        this.userName = userName;
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
        final CustomerDTO other = (CustomerDTO) obj;
        if (this.customerId != other.customerId)
        {
            return false;
        }
        if (!Objects.equals(this.name, other.name))
        {
            return false;
        }
        if (!Objects.equals(this.password, other.password))
        {
            return false;
        }
        if (!Objects.equals(this.userName, other.userName))
        {
            return false;
        }
        return true;
    }

    public int getCustomerId()
    {
        return customerId;
    }

    public String getName()
    {
        return name;
    }

    public HashMap<Integer, OrderDTO> getOrders()
    {
        return orders;
    }

    public String getPassword()
    {
        return password;
    }

    public String getUserName()
    {
        return userName;
    }

    @Override
    public int hashCode()
    {
        int hash = 7;
        hash = 71 * hash + this.customerId;
        hash = 71 * hash + Objects.hashCode(this.name);
        hash = 71 * hash + Objects.hashCode(this.password);
        hash = 71 * hash + Objects.hashCode(this.userName);
        return hash;
    }

    public void setOrders(HashMap<Integer, OrderDTO> orders)
    {
        this.orders = orders;
    }
}
