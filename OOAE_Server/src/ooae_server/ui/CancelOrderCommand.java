/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ooae_server.ui;

import com.google.gson.Gson;
import ooae_server.DTO_Factory;
import ooae_server.entity.Order;

/**
 *
 * @author f023507i
 */
public class CancelOrderCommand implements Command
{

    private String outStr;
    private String jsonInStr;

    CancelOrderCommand(String jsonInStr, String defaultOutStr)
    {
        this.outStr = outStr;
        this.jsonInStr = jsonInStr;
    }

    @Override
    public String execute()
    {
        try
        {
            int orderId = new Gson().fromJson(jsonInStr, Integer.class);
            Order order = Order.findOrder(orderId);
            Order cancelledOrder = null;
            if (order != null)
            {
                cancelledOrder = order.cancel();
            }
            outStr = new Gson().toJson(DTO_Factory.create(cancelledOrder, null));
        } catch (Exception ex)
        {
            System.out.println(ex.getMessage());
            outStr = "ERROR: Unrecognised command";
        }
        return outStr;
    }
}
