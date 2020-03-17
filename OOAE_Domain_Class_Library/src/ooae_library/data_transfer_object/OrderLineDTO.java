package ooae_library.data_transfer_object;

import java.io.Serializable;
import java.util.Objects;

/**
 *
 * @author gdm1
 */
public class OrderLineDTO implements Serializable
{
    private final ItemDTO item;
    private final transient OrderDTO order;
    private final int orderLineId;
    private final double price;
    private final int quantity;

    public OrderLineDTO(
            ItemDTO item, 
            OrderDTO order, 
            int orderLineId, 
            double price, 
            int quantity)
    {
        this.item = item;
        this.order = order;
        this.orderLineId = orderLineId;
        this.price = price;
        this.quantity = quantity;
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
        final OrderLineDTO other = (OrderLineDTO) obj;
        if (this.orderLineId != other.orderLineId)
        {
            return false;
        }
        if (Double.doubleToLongBits(this.price) != Double.doubleToLongBits(other.price))
        {
            return false;
        }
        if (this.quantity != other.quantity)
        {
            return false;
        }
        if (!Objects.equals(this.item, other.item))
        {
            return false;
        }
        return true;
    }

    public ItemDTO getItem()
    {
        return item;
    }

    public OrderDTO getOrder()
    {
        return order;
    }

    public int getOrderLineId()
    {
        return orderLineId;
    }

    public double getPrice()
    {
        return price;
    }

    public int getQuantity()
    {
        return quantity;
    }

    @Override
    public int hashCode()
    {
        int hash = 7;
        hash = 67 * hash + Objects.hashCode(this.item);
        hash = 67 * hash + this.orderLineId;
        hash = 67 * hash + (int) (Double.doubleToLongBits(this.price) ^ (Double.doubleToLongBits(this.price) >>> 32));
        hash = 67 * hash + this.quantity;
        return hash;
    }
}
