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

                    outStr = new Gson().toJson(convertItemToDTO(insertedItem));
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

                    outStr = new Gson().toJson(convertOrderToDTO(shippedOrder, null));
                    break;

                case "updateItemQuantityInStock":
                    ItemDTO item2 = new Gson().fromJson(jsonInStr, ItemDTO.class);

                    Item itemToUpdate = new Item(item2);

                    Item updatedItem = itemToUpdate.updateItemQuantityInStock();

                    outStr = new Gson().toJson(convertItemToDTO(updatedItem));
                    break;

                case "viewAllItems":
                    ArrayList<Item> allItems = Item.findAllItems();

                    outStr = new Gson().toJson(convertItemsToDTO(allItems));
                    break;

                case "viewItem":
                    int itemId = new Gson().fromJson(jsonInStr, Integer.class);

                    Item itemToView = Item.findItem(itemId);

                    outStr = new Gson().toJson(convertItemToDTO(itemToView));
                    break;

                case "viewItemsToReorder":
                    ArrayList<Item> itemsToReorder = Item.findItemsToReorder();

                    outStr = new Gson().toJson(convertItemsToDTO(itemsToReorder));
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

    private ItemDTO convertItemToDTO(Item item)
    {
        ItemDTO itemDTO
                = new ItemDTO(
                        item.getDescription(),
                        item.getItemId(),
                        item.getName(),
                        item.getPrice(),
                        item.getQuantityInStock(),
                        item.getStockReorderLevel(),
                        convertSupplierToDTO(item.getSupplier()));

        return itemDTO;
    }

    private ArrayList<ItemDTO> convertItemsToDTO(ArrayList<Item> items)
    {
        ArrayList<ItemDTO> list = new ArrayList<>(items.size());
        for (Item item : items)
        {
            list.add(convertItemToDTO(item));
        }

        return list;
    }

    private OrderDTO convertOrderToDTO(Order order, CustomerDTO custDTO)
    {
        if (order == null)
        {
            return null;
        }
        
        OrderDTO orderDTO
                = new OrderDTO(
                        custDTO,
                        order.getOrderDateTime(),
                        order.getOrderId(),
                        order.getStatus().toString());

        orderDTO.setOrderLines(
                convertOrderLinesToDTO(order.getOrderLines(), orderDTO));

        return orderDTO;
    }

    private OrderLineDTO convertOrderLineToDTO(OrderLine line, OrderDTO orderDTO)
    {
        return new OrderLineDTO(
                convertItemToDTO(line.getItem()),
                orderDTO,
                line.getOrderLineId(),
                line.getPrice(),
                line.getQuantity());
    }

    private HashMap<Integer, OrderLineDTO> convertOrderLinesToDTO(HashMap<Integer, OrderLine> lines, OrderDTO orderDTO)
    {
        if (lines == null)
        {
            return null;
        }

        HashMap<Integer, OrderLineDTO> orderLinesDTO = new HashMap<>(lines.size());
        for (OrderLine line : lines.values())
        {
            OrderLineDTO lineDTO = convertOrderLineToDTO(line, orderDTO);
            orderLinesDTO.put(lineDTO.getOrderLineId(), lineDTO);
        }
        return orderLinesDTO;
    }

    private SupplierDTO convertSupplierToDTO(Supplier supplier)
    {
        if (supplier == null)
        {
            return null;
        }
        
        SupplierDTO supplierDTO
                = new SupplierDTO(
                        supplier.getName(),
                        supplier.getSupplierId());

        return supplierDTO;
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
