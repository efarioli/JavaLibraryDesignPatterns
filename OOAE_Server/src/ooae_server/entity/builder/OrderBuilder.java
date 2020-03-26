/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ooae_server.entity.builder;

import java.util.Calendar;
import java.util.HashMap;
import ooae_server.entity.Customer;
import ooae_server.entity.Order;
import ooae_server.entity.OrderLine;
import ooae_server.state.OrderState;
import ooae_server.state.OrderStatusFactory;

/**
 *
 * @author f023507i
 */
public class OrderBuilder
{

    private transient Customer customer;
    private int nextOrderLineId = 1;
    private Calendar orderDateTime;
    private int orderId;
    private HashMap<Integer, OrderLine> orderLines;
    private OrderState status;
    private Order order;

    public OrderBuilder()
    {
        customer = null;
        orderDateTime = Calendar.getInstance();
        orderId = -1;
        orderLines = new HashMap<>();
        order = new Order();
    }

    public OrderBuilder withCustomer(Customer c)
    {
        this.customer = c;
        return this;
    }

    public OrderBuilder withOrderDateTime(Calendar odt)
    {
        this.orderDateTime = odt;
        return this;
    }

    public OrderBuilder withOrderId(int orderId)
    {
        this.orderId = orderId;
        return this;
    }

    public OrderBuilder withStatus(OrderState status)
    {
        this.status = status;
        return this;
    }

    public OrderBuilder withStatus(String status)
    {
        this.status = OrderStatusFactory.create(status);
        return this;
    }

    public OrderBuilder withAddOrderLine(OrderLine orderLine)
    {
        OrderLine newOrderLine = new OrderLine(
                orderLine.getItem(),
                order,
                orderLine.getOrderLineId() == -1
                ? nextOrderLineId++
                : orderLine.getOrderLineId(),
                orderLine.getPrice(),
                orderLine.getQuantity());

        order.addOrderLine(newOrderLine);
        return this;
    }

    public Order build()
    {
        order.setCustomer(customer);
        order.setOrderDateTime(orderDateTime);
        order.setOrderId(orderId);
        order.setStatus(status.toString());
        return order;
    }

}
