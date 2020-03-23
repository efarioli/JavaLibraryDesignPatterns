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
public class ViewItemsToReorderCommand implements Command
{

    private String outStr;

    public ViewItemsToReorderCommand(String outStr)
    {
        this.outStr = outStr;
    }

    @Override
    public String execute()
    {
        try
        {
            ArrayList<Item> itemsToReorder = Item.findItemsToReorder();
            outStr = new Gson().toJson(DTO_Factory.create(itemsToReorder));
        } catch (Exception ex)
        {
            System.out.println(ex.getMessage());
            outStr = "ERROR: Unrecognised command";
        }
        return outStr;
    }
}
