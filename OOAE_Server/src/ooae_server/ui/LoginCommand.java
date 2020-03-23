/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ooae_server.ui;

import com.google.gson.Gson;
import ooae_library.data_transfer_object.CustomerDTO;
import ooae_server.DTO_Factory;
import ooae_server.entity.Customer;

/**
 *
 * @author f023507i
 */
public class LoginCommand implements Command
{

    private String outStr;
    private String jsonInStr;

    LoginCommand(String jsonInStr, String defaultOutStr)
    {
        this.jsonInStr = jsonInStr;
        this.outStr = outStr;
    }

    @Override
    public String execute()
    {
        try
        {
            CustomerDTO customerDTO = new Gson().fromJson(jsonInStr, CustomerDTO.class);
            Customer customer = new Customer(customerDTO);
            Customer user = customer.login();
            outStr = new Gson().toJson(DTO_Factory.create(user));
        } catch (Exception ex)
        {
            System.out.println(ex.getMessage());
            outStr = "ERROR: Unrecognised command";
        }
        return outStr;
    }

}
