/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ooae_customer_client.visitor;

import java.util.Calendar;
import java.util.HashMap;
import ooae_library.data_transfer_object.CustomerDTO;
import ooae_library.data_transfer_object.ItemDTO;
import ooae_library.data_transfer_object.OrderDTO;
import ooae_library.data_transfer_object.OrderLineDTO;

/**
 *
 * @author f023507i
 */
public class OrderDTOVisitable implements Visitable
{
    private OrderDTO  order;
    

    public OrderDTOVisitable(OrderDTO order)
    {         
        this.order = order;
    }
 
    @Override
    public void accept(Visitor visitor)
    {
        visitor.visit(this);
    }
    
    public void addOrderLine(int orderLineId, ItemDTO item, double price, int quantity)
    {
        order.addOrderLine(orderLineId, item, price, quantity);
    }

    @Override
    public boolean equals(Object obj)
    {
       return order.equals(obj);
    }

  

    public CustomerDTO getCustomer()
    {
        return order.getCustomer();
    }

    public Calendar getOrderDateTime()
    {
        return order.getOrderDateTime();
    }

    public int getOrderId()
    {
        return order.getOrderId();
    }

    public HashMap<Integer, OrderLineDTO> getOrderLines()
    {
        return order.getOrderLines();
    }

    public String getStatus()
    {
        return order.getStatus();
    }

    @Override
    public int hashCode()
    {
        return order.hashCode();
    }

    public boolean isEmpty()
    {
        return order.isEmpty();
    }

    public void setOrderLines(HashMap<Integer, OrderLineDTO> orderLines)
    {
        order.setOrderLines(orderLines);
    }

}
