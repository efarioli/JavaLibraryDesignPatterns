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
import ooae_server.entity.Customer;
import ooae_server.entity.Item;
import ooae_server.entity.Order;
import ooae_server.entity.OrderLine;
import ooae_server.entity.Supplier;

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
                case "cancelOrder":
                    int orderId = new Gson().fromJson(jsonInStr, Integer.class);

                    Order order = Order.findOrder(orderId);
                    Order cancelledOrder = null;

                    if (order != null)
                    {
                        cancelledOrder = order.cancel();
                    }

                    outStr = new Gson().toJson(convertOrderToDTO(cancelledOrder, null));
                    break;

                case "login":
                    CustomerDTO customerDTO = new Gson().fromJson(jsonInStr, CustomerDTO.class);

                    Customer customer = new Customer(customerDTO);

                    Customer user = customer.login();

                    outStr = new Gson().toJson(convertCustomerToDTO(user));
                    break;

                case "placeOrder":
                    CustomerDTO customerDTO1 = new Gson().fromJson(jsonInStr, CustomerDTO.class);

                    Customer customer1 = new Customer(customerDTO1);

                    Order orderToInsert = customer1.getOrders().get(-1);

                    Order insertedOrder = orderToInsert.insert();

                    outStr = new Gson().toJson(convertOrderToDTO(insertedOrder, null));
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

                case "viewMyOrders":
                    CustomerDTO customerDTO2 = new Gson().fromJson(jsonInStr, CustomerDTO.class);

                    Customer customer2 = new Customer(customerDTO2);

                    HashMap<Integer, Order> myOrders = Order.findOrdersForCustomer(customer2);

                    outStr = new Gson().toJson(convertOrdersToDTO(myOrders, null));
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

    private CustomerDTO convertCustomerToDTO(Customer customer)
    {
        if (customer == null)
        {
            return null;
        }

        CustomerDTO custDTO
                = new CustomerDTO(
                        customer.getCustomerId(),
                        customer.getName(),
                        customer.getPassword(),
                        customer.getUserName());

        custDTO.setOrders(
                convertOrdersToDTO(customer.getOrders(), custDTO));
        return custDTO;
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

    private HashMap<Integer, OrderDTO> convertOrdersToDTO(HashMap<Integer, Order> orders, CustomerDTO custDTO)
    {
        if (orders == null)
        {
            return null;
        }

        HashMap<Integer, OrderDTO> ordersDTO = new HashMap<>();
        for (Order order : orders.values())
        {
            OrderDTO orderDTO = convertOrderToDTO(order, custDTO);

            ordersDTO.put(orderDTO.getOrderId(), orderDTO);
        }
        return ordersDTO;
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
}
