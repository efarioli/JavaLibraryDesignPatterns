/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ooae_server.ui;

import com.google.gson.Gson;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import ooae_library.data_transfer_object.CustomerDTO;
import ooae_server.DTO_Factory;
import ooae_server.entity.Customer;
import ooae_server.entity.Order;

/**
 *
 * @author f023507i
 */
public class ViewMyOrdersCommand implements Command
{

    private String outStr;
    private String jsonInStr;

    public ViewMyOrdersCommand(String jsonInStr, String defaultOutStr)
    {
        this.jsonInStr = jsonInStr;
        this.outStr = outStr;
    }

    @Override
    public String execute()
    {
        try
        {
            CustomerDTO customerDTO2 = new Gson().fromJson(jsonInStr, CustomerDTO.class);

            Customer customer2 = new Customer(customerDTO2);

            HashMap<Integer, Order> myOrders = Order.findOrdersForCustomer(customer2);

            outStr = new Gson().toJson(DTO_Factory.create(myOrders, null));
        } catch (Exception ex)
        {
            System.out.println(ex.getMessage());
            outStr = "ERROR: Unrecognised command";
        }
        return outStr;
    }

}
