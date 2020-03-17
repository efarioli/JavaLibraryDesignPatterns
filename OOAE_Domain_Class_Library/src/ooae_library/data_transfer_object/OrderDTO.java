package ooae_library.data_transfer_object;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Objects;

/**
 *
 * @author gdm1
 */
public class OrderDTO implements Serializable
{

    private final transient CustomerDTO customer;
    private transient int nextOrderLineId = 1;
    private final Calendar orderDateTime;
    private final int orderId;
    private HashMap<Integer, OrderLineDTO> orderLines = new HashMap<>();
    private final String status;

    public OrderDTO(CustomerDTO customer, Calendar orderDateTime, int orderId, String status)
    {
        this.customer = customer;
        this.orderDateTime = orderDateTime;
        this.orderId = orderId;
        this.status = status;
    }

    public void addOrderLine(int orderLineId, ItemDTO item, double price, int quantity)
    {
        OrderLineDTO orderLine = findOrderLineForItem(item);
        if (orderLine != null)
        {
            int newQuantity = orderLine.getQuantity() + quantity;

            if (newQuantity > 0)
            {
                orderLine
                        = new OrderLineDTO(
                                orderLine.getItem(),
                                orderLine.getOrder(),
                                orderLine.getOrderLineId(),
                                orderLine.getPrice(),
                                newQuantity);
                orderLines.put(orderLine.getOrderLineId(), orderLine);
            }
            else
            {
                //remove order line for this item
                orderLines.remove(orderLine.getOrderLineId());
            }
        }
        else
        {
            orderLine
                    = new OrderLineDTO(
                            item,
                            this,
                            orderLineId == -1 ? nextOrderLineId++ : orderLineId,
                            price,
                            quantity);
            orderLines.put(orderLine.getOrderLineId(), orderLine);
        }
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
        final OrderDTO other = (OrderDTO) obj;
        if (this.orderId != other.orderId)
        {
            return false;
        }
        if (!Objects.equals(this.status, other.status))
        {
            return false;
        }
        if (!Objects.equals(this.customer, other.customer))
        {
            return false;
        }
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        if (!Objects.equals(sdf.format(this.orderDateTime.getTime()), sdf.format(other.orderDateTime.getTime())))
        {
            return false;
        }
        if (!Objects.equals(this.orderLines, other.orderLines))
        {
            return false;
        }
        return true;
    }

    private OrderLineDTO findOrderLineForItem(ItemDTO item)
    {
        OrderLineDTO line = null;

        if (!orderLines.isEmpty())
        {
            Iterator<OrderLineDTO> lines = orderLines.values().iterator();

            while (lines.hasNext() && line == null)
            {
                OrderLineDTO ol = lines.next();
                if (ol.getItem().getItemId() == item.getItemId())
                {
                    line = ol;
                }
            }
        }

        return line;
    }

    public CustomerDTO getCustomer()
    {
        return customer;
    }

    public Calendar getOrderDateTime()
    {
        return orderDateTime;
    }

    public int getOrderId()
    {
        return orderId;
    }

    public HashMap<Integer, OrderLineDTO> getOrderLines()
    {
        return orderLines;
    }

    public String getStatus()
    {
        return status;
    }

    @Override
    public int hashCode()
    {
        int hash = 5;
        hash = 97 * hash + Objects.hashCode(this.customer);
        hash = 97 * hash + Objects.hashCode(this.orderDateTime);
        hash = 97 * hash + this.orderId;
        hash = 97 * hash + Objects.hashCode(this.orderLines);
        hash = 97 * hash + Objects.hashCode(this.status);
        return hash;
    }

    public boolean isEmpty()
    {
        return orderLines.isEmpty();
    }

    public void setOrderLines(HashMap<Integer, OrderLineDTO> orderLines)
    {
        this.orderLines = orderLines;
    }
}
