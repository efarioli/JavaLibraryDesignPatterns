package ooae_server.entity.builder;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
import ooae_library.data_transfer_object.OrderLineDTO;
import ooae_server.entity.Item;
import ooae_server.entity.Order;
import ooae_server.entity.OrderLine;

/**
 *
 * @author f023507i
 */
public class OrderLineBuilder
{

    private Item item;
    private Order order;
    private int orderLineId;
    private double price;
    private int quantity;
    private OrderLine orderLine;

    public OrderLineBuilder()
    {
        item =null;
        order = null;
        price = 0;
        quantity = 0;
        orderLineId = -1;
    }

    public OrderLineBuilder withItem(Item item)
    {
        this.item = item;
        return this;
    }
    public OrderLineBuilder withOrderLineId(int orderLineId )
    {
        this.orderLineId = orderLineId;
        return this;
    }
    public OrderLineBuilder withPrice(double price )
    {
        this.price = price;
        return this;
    }
     public OrderLineBuilder withQuantity(int quantity )
    {
        this.quantity = quantity;
        return this;
    }
    public OrderLine build()
    {
        orderLine = new OrderLine(item, order, orderLineId, price, quantity);

        return orderLine;
    }

}
