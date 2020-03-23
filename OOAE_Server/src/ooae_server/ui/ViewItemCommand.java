/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ooae_server.ui;

import com.google.gson.Gson;
import ooae_server.DTO_Factory;
import ooae_server.entity.Item;

/**
 *
 * @author f023507i
 */
public class ViewItemCommand implements Command
{

    private String outStr;
    private String jsonInStr;

    public ViewItemCommand(String jsonInStr, String outStr)
    {
        this.outStr = outStr;
        this.jsonInStr = jsonInStr;
    }

    @Override
    public String execute()
    {
        try
        {
            int itemId = new Gson().fromJson(jsonInStr, Integer.class);
            Item itemToView = Item.findItem(itemId);
            outStr = new Gson().toJson(DTO_Factory.create(itemToView));
        } catch (Exception ex)
        {
            System.out.println(ex.getMessage());
            outStr = "ERROR: Unrecognised command";
        }
        return outStr;
    }
}
