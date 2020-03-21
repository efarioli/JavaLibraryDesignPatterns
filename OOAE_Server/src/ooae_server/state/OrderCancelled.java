/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ooae_server.state;

import ooae_server.entity.Order;

/**
 *
 * @author f023507i
 */
public class OrderCancelled extends OrderState
{

    public OrderCancelled(String state)
    {
        super(state);
    }
  

    @Override
    public Order ship(Order order) throws Exception
    {
        return null;
    }

    @Override
    public Order cancel(Order order) throws Exception
    {
        return null;
    }

}
