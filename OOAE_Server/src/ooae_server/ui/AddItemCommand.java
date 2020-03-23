/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ooae_server.ui;

import com.google.gson.Gson;
import ooae_library.data_transfer_object.ItemDTO;
import ooae_server.DTO_Factory;
import ooae_server.entity.Item;

/**
 *
 * @author f023507i
 */
public class AddItemCommand implements Command
{

    private String outStr;
    private String jsonInStr;

    public AddItemCommand(String jsonInStr, String outStr)
    {
        this.jsonInStr = jsonInStr;
        this.outStr = outStr;
    }

    @Override
    public String execute()
    {
        try
        {
            ItemDTO item1 = new Gson().fromJson(jsonInStr, ItemDTO.class);
            System.out.println(item1);
            Item item = new Item(item1);
            Item insertedItem = item.insert();
            outStr = new Gson().toJson(DTO_Factory.create(insertedItem));
        } catch (Exception ex)
        {
            System.out.println(ex.getMessage());
            outStr = "ERROR: Unrecognised command";
        }
        return outStr;
    }
}
