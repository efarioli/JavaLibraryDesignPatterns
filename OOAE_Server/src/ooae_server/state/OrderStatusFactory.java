/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ooae_server.state;

/**
 *
 * @author f023507i
 */
public class OrderStatusFactory
{

    public static OrderState create(String str)
    {
        if (str.equals("Placed"))
        {
            return new OrderPlaced(str);
        } else if (str.equals("Cancelled"))
        {
            return new OrderCancelled(str);
        } else if (str.equals("Shipped"))
        {
            return new OrderShipped(str);
        }
        return new OrderNull(str);
    }

    public static String create(OrderPlaced op)
    {
        return "Placed";
    }

    public static String create(OrderShipped os)
    {
        return "Shipped";
    }
     public static String create(OrderCancelled oc)
    {
        return "Cancelled";
    }
      public static String create(OrderNull on)
    {
        return "";
    }
}
