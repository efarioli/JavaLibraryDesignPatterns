package ooae_customer_client;

import ooae_customer_client.visitor.OrderDTOVisitable;
import ooae_customer_client.visitor.FormatOrderAsString;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.lang.reflect.Type;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import ooae_library.data_transfer_object.CustomerDTO;
import ooae_library.data_transfer_object.ItemDTO;
import ooae_library.data_transfer_object.OrderDTO;
import ooae_library.data_transfer_object.OrderLineDTO;
import ooae_library.keyboard_io.KeyboardInputter;

/**
 *
 * @author gdm1
 */
public class OOAE_Customer_Client
{

    private static final String HOSTNAME = "localhost";
    private static final int PORT_NUMBER = 30000;


    private static final int LOGIN = 1;
    private static final int VIEW_ALL_ITEMS = 2;
    private static final int VIEW_ITEM = 3;
    private static final int VIEW_CART = 4;
    private static final int PLACE_ORDER = 5;
    private static final int CLEAR_CART = 6;
    private static final int VIEW_MY_ORDERS = 7;
    private static final int CANCEL_ORDER = 8;
    private static final int EXIT = 9;

    private static final HashMap<Integer, ItemDTO> itemsCache = new HashMap<>();
    private static OrderDTO nextOrder = new OrderDTO(null, Calendar.getInstance(), -1, "");
    private static CustomerDTO user = null;

    public static void main(String[] args)
    {
        int choice = displayMenu();

        while (choice != EXIT)
        {
            try
            {
                Socket socket = new Socket(HOSTNAME, PORT_NUMBER);
                PrintWriter outToServer = new PrintWriter(socket.getOutputStream(), true);
                BufferedReader inFromServer = new BufferedReader(new InputStreamReader(socket.getInputStream()));

                switch (choice)
                {
                    case CANCEL_ORDER:
                        int orderId = KeyboardInputter.getInteger("Order id");

                        outToServer.println("Customer#cancelOrder#" + new Gson().toJson(orderId));

//                        System.out.println("Server says: " + inFromServer.readLine());
                        OrderDTO cancelledOrder = new Gson().fromJson(inFromServer.readLine(), OrderDTO.class);

                        if (cancelledOrder != null)
                        {
                            displayOrder(cancelledOrder);
                        }
                        break;

                    case CLEAR_CART:
                        if (!nextOrder.isEmpty())
                        {
                            char response
                                    = KeyboardInputter
                                            .getString("\nAre you sure you want to clear the cart? (y/n)")
                                            .toLowerCase()
                                            .charAt(0);

                            if (response == 'y')
                            {
                                clearCart();

                                System.out.println("Your cart is empty");
                            }
                        } else
                        {
                            System.out.println("Your cart is already empty");
                        }
                        break;

                    case LOGIN:
                        String username = KeyboardInputter.getString("Username");
                        String password = KeyboardInputter.getString("Password");
                        CustomerDTO credentials
                                = new CustomerDTO(
                                        -1,
                                        "",
                                        password,
                                        username);

                        outToServer.println("Customer#login#" + new Gson().toJson(credentials));

//                        System.out.println("Server says: " + inFromServer.readLine());
                        user = new Gson().fromJson(inFromServer.readLine(), CustomerDTO.class);

                        if (user.getCustomerId() != -1)
                        {
                            System.out.println("Login was successful");
                        } else
                        {
                            user = null;
                            System.out.println("Login credentials were not recognised");
                        }
                        break;

                    case PLACE_ORDER:
                        if (user != null)
                        {
                            if (!nextOrder.isEmpty())
                            {
                                HashMap<Integer, OrderLineDTO> lines = nextOrder.getOrderLines();

                                nextOrder
                                        = new OrderDTO(
                                                user,
                                                nextOrder.getOrderDateTime(),
                                                nextOrder.getOrderId(),
                                                nextOrder.getStatus());
                                nextOrder.setOrderLines(lines);

                                HashMap<Integer, OrderDTO> orders = new HashMap<>();
                                orders.put(nextOrder.getOrderId(), nextOrder);
                                user.setOrders(orders);

                                outToServer.println("Customer#placeOrder#" + new Gson().toJson(user));

//                                System.out.println("Server says: " + inFromServer.readLine());
                                OrderDTO newOrder = new Gson().fromJson(inFromServer.readLine(), OrderDTO.class);

                                if (newOrder != null)
                                {
                                    displayOrder(newOrder);
                                    clearCart();
                                }
                            } else
                            {
                                System.out.println("Your cart is empty, there is no order to place");
                            }
                        } else
                        {
                            System.out.println("ERROR: You must be logged in to access this feature");
                        }
                        break;

                    case VIEW_ALL_ITEMS:
                        outToServer.println("Customer#viewAllItems");

//                        System.out.println("Server says: " + inFromServer.readLine());
                        Type listType = new TypeToken<ArrayList<ItemDTO>>()
                        {
                        }.getType();
                        ArrayList<ItemDTO> list = new Gson().fromJson(inFromServer.readLine(), listType);

                        itemsCache.clear();
                        for (ItemDTO item : list)
                        {
                            itemsCache.put(item.getItemId(), item);
                            displayItemSummary(item);
                        }
                        break;

                    case VIEW_CART:
                        displayOrder(nextOrder);
                        break;

                    case VIEW_ITEM:
                        int itemId = KeyboardInputter.getInteger("Item id");
                        ItemDTO item;
                        if (itemsCache.containsKey(itemId))
                        {
                            item = itemsCache.get(itemId);
                        } else
                        {
                            outToServer.println("Customer#viewItem#" + new Gson().toJson(itemId));

//                            System.out.println("Server says: " + inFromServer.readLine());
                            item = new Gson().fromJson(inFromServer.readLine(), ItemDTO.class);
                        }

                        if (item != null)
                        {
                            displayItem(item);

                            char response
                                    = KeyboardInputter
                                            .getString("\nAdd to cart? (y/n)")
                                            .toLowerCase()
                                            .charAt(0);

                            if (response == 'y')
                            {
                                int qtyToOrder = KeyboardInputter.getInteger("Quantity to order");
                                nextOrder.addOrderLine(-1, item, item.getPrice(), qtyToOrder);

                                System.out.println("Item added to cart");
                            }
                        }
                        break;

                    case VIEW_MY_ORDERS:
                        if (user != null)
                        {
                            outToServer.println("Customer#viewMyOrders#" + new Gson().toJson(user));

//                            System.out.println("Server says: " + inFromServer.readLine());
                            Type listType1 = new TypeToken<HashMap<Integer, OrderDTO>>()
                            {
                            }.getType();
                            HashMap<Integer, OrderDTO> myOrders = new Gson().fromJson(inFromServer.readLine(), listType1);

                            for (OrderDTO order1 : myOrders.values())
                            {
                                displayOrder(order1);
                            }
                        } else
                        {
                            System.out.println("ERROR: You must be logged in to access this feature");
                        }
                        break;

                    default:
                        break;
                }

                socket.close();
            } catch (IOException ioe)
            {
                System.out.println(ioe.getMessage());
            }
            choice = displayMenu();
        }
    }

    private static void clearCart()
    {
        nextOrder = new OrderDTO(user, Calendar.getInstance(), -1, "");
        itemsCache.clear();
    }

    private static void displayItem(ItemDTO item)
    {
        System.out.println("\nId: " + item.getItemId());
        System.out.println("\tName: " + item.getName());
        System.out.println("\tDescription: " + item.getDescription());
        System.out.println("\tPrice: " + item.getPrice());
        System.out.println("\tQuantity in stock: " + item.getQuantityInStock());
    }

    private static void displayItemSummary(ItemDTO item)
    {
        System.out.println("\nId: " + item.getItemId());
        System.out.println("\tName: " + item.getName());
        System.out.println("\tPrice: " + item.getPrice());
    }

    private static int displayMenu()
    {
        System.out.println("\nCustomer menu");
        System.out.println("1. Login");
        System.out.println("2. View all items");
        System.out.println("3. View item");
        System.out.println("4. View cart");
        System.out.println("5. Submit order");
        System.out.println("6. Clear cart");
        System.out.println("7. View my orders");
        System.out.println("8. Cancel order");
        System.out.println("9. Exit");

        return KeyboardInputter.getInteger("\nOption");
    }

    protected static void displayOrder(OrderDTO order)
    {
        OrderDTOVisitable visitable = new OrderDTOVisitable(order);
        FormatOrderAsString visitor = new FormatOrderAsString();
        visitable.accept(visitor);
        System.out.println(
                visitor.visit(visitable)
        );

    }
}
