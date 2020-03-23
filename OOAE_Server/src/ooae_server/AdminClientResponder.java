package ooae_server;

import com.google.gson.Gson;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import ooae_library.data_transfer_object.ItemDTO;
import ooae_server.database.AdminClientResponderGateway;
import ooae_server.entity.*;
import ooae_server.database.CustomerGateway;
import ooae_server.database.ItemGateway;
import ooae_server.database.OrderGateway;
import ooae_server.database.SupplierGateway;

/**
 *
 * @author gdm1
 */
public class AdminClientResponder implements Runnable
{

    private final PrintWriter clientOut;
    private final String command;
    private final String jsonInStr;
    private final Socket socket;
    private final AdminClientResponderGateway acResponderGateway = new AdminClientResponderGateway();

    public AdminClientResponder(Socket socket, PrintWriter clientOut, String[] inputParts)
    {
        this.socket = socket;
        this.clientOut = clientOut;
        command = inputParts[1];
        if (inputParts.length > 2)
        {
            jsonInStr = inputParts[2];
        } else
        {
            jsonInStr = null;
        }
    }

    @Override
    public void run()
    {
        String outStr = "OK";
        try
        {
            switch (command)
            {
                case "addItem":
                    ItemDTO item1 = new Gson().fromJson(jsonInStr, ItemDTO.class);

                    Item item = new Item(item1);

                    Item insertedItem = item.insert();

                    outStr = new Gson().toJson(DTO_Factory.create(insertedItem));
                    break;

                case "checkNeedForDatabaseInitialisation":
                    checkNeedForDatabaseInitialisation();
                    break;

                case "initialiseDatabase":
                    initialiseDatabase();
                    break;

                case "shipOrder":
                    int orderId = new Gson().fromJson(jsonInStr, Integer.class);

                    Order order = Order.findOrder(orderId);
                    Order shippedOrder = null;

                    if (order != null)
                    {
                        shippedOrder = order.ship();
                    }

                    outStr = new Gson().toJson(DTO_Factory.create(shippedOrder, null));
                    break;

                case "updateItemQuantityInStock":
                    ItemDTO item2 = new Gson().fromJson(jsonInStr, ItemDTO.class);

                    Item itemToUpdate = new Item(item2);

                    Item updatedItem = itemToUpdate.updateItemQuantityInStock();

                    outStr = new Gson().toJson(DTO_Factory.create(updatedItem));
                    break;

                case "viewAllItems":
                    ArrayList<Item> allItems = Item.findAllItems();

                    outStr = new Gson().toJson(DTO_Factory.create(allItems));
                    break;

                case "viewItem":
                    int itemId = new Gson().fromJson(jsonInStr, Integer.class);

                    Item itemToView = Item.findItem(itemId);

                    outStr = new Gson().toJson(DTO_Factory.create(itemToView));
                    break;

                case "viewItemsToReorder":
                    ArrayList<Item> itemsToReorder = Item.findItemsToReorder();

                    outStr = new Gson().toJson(DTO_Factory.create(itemsToReorder));
                    break;

                default:
                    outStr = "ERROR: Unrecognised command";
                    break;
            }
            clientOut.println(outStr);
            clientOut.flush();
            if (socket != null)
            {
                socket.close();
            }
        } catch (Exception e)
        {
//            e.printStackTrace();
            clientOut.println(e.getMessage());
            clientOut.flush();
        }
    }

    private void checkNeedForDatabaseInitialisation() throws Exception
    {
        acResponderGateway.checkNeedForDatabaseInitialisation();

    }

    private void initialiseDatabase() throws Exception
    {
        acResponderGateway.initialiseDatabase();
    }
}
