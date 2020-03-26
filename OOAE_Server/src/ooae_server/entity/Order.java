package ooae_server.entity;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import ooae_library.data_transfer_object.OrderDTO;
import ooae_library.data_transfer_object.OrderLineDTO;
import ooae_server.database.OrderGateway;
/**
 *
 * @author gdm1
 */
import ooae_server.state.OrderNull;
import ooae_server.state.OrderPlaced;
import ooae_server.state.OrderState;
import ooae_server.state.OrderStatusFactory;

public class Order
{

    private transient Customer customer;
    private int nextOrderLineId = 1;
    private Calendar orderDateTime;
    private int orderId;
    private final HashMap<Integer, OrderLine> orderLines;
    private OrderState status;

    public Order()
    {
        customer = null;
        orderDateTime = Calendar.getInstance();
        orderId = -1;
        orderLines = new HashMap<>();
    }

    public Order(OrderDTO order)
    {
        this.orderDateTime = order.getOrderDateTime();
        this.orderId = order.getOrderId();
        this.status = OrderStatusFactory.create(order.getStatus());

        this.orderLines = new HashMap<>();
        for (OrderLineDTO line : order.getOrderLines().values())
        {
            OrderLine ol = new OrderLine(line);
            ol.setOrder(this);
            orderLines.put(ol.getOrderLineId(), ol);
        }
    }

    public void addOrderLine(int orderLineId, Item item, double price, int quantity)
    {
        OrderLine orderLine = findOrderLineForItem(item);
        if (orderLine != null)
        {
            orderLine.updateQuantity(quantity);
            if (orderLine.getQuantity() <= 0)
            {
                //remove order line for this item
                orderLines.remove(orderLine.getOrderLineId());
            }
        } else
        {
            orderLine
                    = new OrderLine(
                            item,
                            this,
                            orderLineId == -1
                                    ? nextOrderLineId++
                                    : orderLineId,
                            price,
                            quantity);
            orderLines.put(orderLine.getOrderLineId(), orderLine);
        }
    }

    public Order cancel() throws Exception
    {
        return status.cancel(this);
    }

    public static Order findOrder(int orderId) throws Exception
    {
        OrderGateway orderTable = new OrderGateway();

        return orderTable.findOrder(orderId);
    }

    private OrderLine findOrderLineForItem(Item item)
    {
        OrderLine line = null;

        if (!orderLines.isEmpty())
        {
            Iterator<OrderLine> lines = orderLines.values().iterator();

            while (lines.hasNext() && line == null)
            {
                OrderLine ol = lines.next();
                if (ol.getItem().getItemId() == item.getItemId())
                {
                    line = ol;
                }
            }
        }

        return line;
    }

    public static HashMap<Integer, Order> findOrdersForCustomer(Customer customer) throws Exception
    {
        OrderGateway orderTable = new OrderGateway();

        return orderTable.getOrdersForCustomer(customer);
    }

    public Customer getCustomer()
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

    public HashMap<Integer, OrderLine> getOrderLines()
    {
        return orderLines;
    }

    public OrderState getStatus()
    {
        return status;
    }

    public Order insert() throws Exception
    {
        OrderGateway orderTable = new OrderGateway();

        return orderTable.insertOrder(this);
    }

    public boolean isEmpty()
    {
        return orderLines.isEmpty();
    }

    public void setCustomer(Customer customer)
    {
        this.customer = customer;
    }

    public void setOrderDateTime(Calendar orderDateTime)
    {
        this.orderDateTime = orderDateTime;
    }

    public void setOrderId(int orderId)
    {
        this.orderId = orderId;
    }

    public void setStatus(String status)
    {
        this.status = OrderStatusFactory.create(status);
    }

    public Order ship() throws Exception
    {
        return status.ship(this);
    }

    public void addOrderLine(OrderLine newOrderLine)
    {
        orderLines.put(newOrderLine.getOrderLineId(), newOrderLine);
    }
}
