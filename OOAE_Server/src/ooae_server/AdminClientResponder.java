package ooae_server;

import com.google.gson.Gson;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import ooae_library.data_transfer_object.CustomerDTO;
import ooae_library.data_transfer_object.ItemDTO;
import ooae_library.data_transfer_object.OrderDTO;
import ooae_library.data_transfer_object.OrderLineDTO;
import ooae_library.data_transfer_object.SupplierDTO;
import ooae_server.entity.Item;
import ooae_server.entity.Order;
import ooae_server.database.CustomerGateway;
import ooae_server.database.ItemGateway;
import ooae_server.database.OrderGateway;
import ooae_server.database.SupplierGateway;
import ooae_server.entity.OrderLine;
import ooae_server.entity.Supplier;

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

    public AdminClientResponder(Socket socket, PrintWriter clientOut, String[] inputParts)
    {
        this.socket = socket;
        this.clientOut = clientOut;
        command = inputParts[1];
        if (inputParts.length > 2)
        {
            jsonInStr = inputParts[2];
        }
        else
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
        }
        catch (Exception e)
        {
//            e.printStackTrace();
            clientOut.println(e.getMessage());
            clientOut.flush();
        }
    }

    private void checkNeedForDatabaseInitialisation() throws Exception
    {
        CustomerGateway customerTable = new CustomerGateway();
        ItemGateway itemTable = new ItemGateway();
        OrderGateway orderTable = new OrderGateway();
        SupplierGateway supplierTable = new SupplierGateway();

        if (!customerTable.exists()
                || !itemTable.exists()
                || !orderTable.exists()
                || !supplierTable.exists())
        {
            initialiseDatabase();
        }
    }

   

    private void initialiseDatabase() throws Exception
    {
        CustomerGateway customerTable = new CustomerGateway();
        ItemGateway itemTable = new ItemGateway();
        OrderGateway orderTable = new OrderGateway();
        SupplierGateway supplierTable = new SupplierGateway();
        String errorMsgs = "";

        try
        {
            orderTable.dropTable();
            itemTable.dropTable();
            supplierTable.dropTable();
            customerTable.dropTable();

            customerTable.initialiseTable();
            supplierTable.initialiseTable();
            itemTable.initialiseTable();
            orderTable.initialiseTable();
        }
        catch (Exception e)
        {
            errorMsgs += e.getMessage() + "\n";
        }

        if (!errorMsgs.isEmpty())
        {
            throw new Exception(errorMsgs);
        }
    }
}
