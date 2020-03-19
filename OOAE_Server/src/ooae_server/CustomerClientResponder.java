package ooae_server;

import com.google.gson.Gson;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import ooae_library.data_transfer_object.CustomerDTO;
import ooae_server.entity.*;

/**
 *
 * @author gdm1
 */
public class CustomerClientResponder implements Runnable
{

    private final String command;
    private final PrintWriter clientOut;
    private final String jsonInStr;
    private final Socket socket;

    public CustomerClientResponder(Socket socket, PrintWriter clientOut, String[] inputParts)
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
                case "cancelOrder":
                    int orderId = new Gson().fromJson(jsonInStr, Integer.class);

                    Order order = Order.findOrder(orderId);
                    Order cancelledOrder = null;

                    if (order != null)
                    {
                        cancelledOrder = order.cancel();
                    }

                    outStr = new Gson().toJson(DTO_Factory.create(cancelledOrder, null));
                    break;

                case "login":
                    CustomerDTO customerDTO = new Gson().fromJson(jsonInStr, CustomerDTO.class);

                    Customer customer = new Customer(customerDTO);

                    Customer user = customer.login();

                    outStr = new Gson().toJson(DTO_Factory.create(user));
                    break;

                case "placeOrder":
                    CustomerDTO customerDTO1 = new Gson().fromJson(jsonInStr, CustomerDTO.class);

                    Customer customer1 = new Customer(customerDTO1);

                    Order orderToInsert = customer1.getOrders().get(-1);

                    Order insertedOrder = orderToInsert.insert();

                    outStr = new Gson().toJson(DTO_Factory.create(insertedOrder, null));
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

                case "viewMyOrders":
                    CustomerDTO customerDTO2 = new Gson().fromJson(jsonInStr, CustomerDTO.class);

                    Customer customer2 = new Customer(customerDTO2);

                    HashMap<Integer, Order> myOrders = Order.findOrdersForCustomer(customer2);

                    outStr = new Gson().toJson(DTO_Factory.create(myOrders, null));
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

}
