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
public class UpdateItemQuantityInStockCommand implements Command
{

    private String outStr;
    private String jsonInStr;

    public UpdateItemQuantityInStockCommand(String jsonInStr, String outStr)
    {
        this.outStr = outStr;
        this.jsonInStr = jsonInStr;
    }

    @Override
    public String execute()
    {
        try
        {
            ItemDTO item2 = new Gson().fromJson(jsonInStr, ItemDTO.class);
            Item itemToUpdate = new Item(item2);
            Item updatedItem = itemToUpdate.updateItemQuantityInStock();
            outStr = new Gson().toJson(DTO_Factory.create(updatedItem));
        } catch (Exception ex)
        {
            System.out.println(ex.getMessage());
            outStr = "ERROR: Unrecognised command";
        }
        return outStr;
    }
}
