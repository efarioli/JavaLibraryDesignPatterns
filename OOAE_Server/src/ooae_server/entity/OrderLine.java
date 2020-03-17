package ooae_server.entity;

import ooae_library.data_transfer_object.OrderLineDTO;

/**
 *
 * @author gdm1
 */
public class OrderLine
{
    private final Item item;
    private Order order;
    private final int orderLineId;
    private final double price;
    private int quantity;

    public OrderLine(OrderLineDTO orderLine)
    {
        this.item = new Item(orderLine.getItem());
        this.orderLineId = orderLine.getOrderLineId();
        this.price = orderLine.getPrice();
        this.quantity = orderLine.getQuantity();
    }

    public OrderLine(
            Item item, 
            Order order, 
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

    public Item getItem()
    {
        return item;
    }

    public Order getOrder()
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

    public void setOrder(Order order)
    {
        this.order = order;
    }

    public void updateQuantity(int quantityIncrement)
    {
        quantity += quantityIncrement;
    }
}
