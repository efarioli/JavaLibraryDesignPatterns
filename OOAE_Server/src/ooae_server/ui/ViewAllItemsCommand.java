/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ooae_server.ui;

import com.google.gson.Gson;
import java.util.ArrayList;
import ooae_server.DTO_Factory;
import ooae_server.entity.Item;

/**
 *
 * @author f023507i
 */
public class ViewAllItemsCommand implements Command
{

    private String outStr;
    private String jsonInStr;

    public ViewAllItemsCommand(String jsonInStr, String outString )
    {
        this.outStr = outStr;
        this.jsonInStr = jsonInStr;
    }

    @Override
    public String execute()
    {
        try
        {
            ArrayList<Item> allItems = Item.findAllItems();

            outStr = new Gson().toJson(DTO_Factory.create(allItems));
        } catch (Exception ex)
        {
            System.out.println(ex.getMessage());
            outStr = "ERROR: Unrecognised command";
        }
        return outStr;

    }

}
