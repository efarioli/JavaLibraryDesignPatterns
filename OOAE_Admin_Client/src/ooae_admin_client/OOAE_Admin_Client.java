package ooae_admin_client;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.lang.reflect.Type;
import java.net.Socket;
import java.util.ArrayList;
import ooae_library.data_transfer_object.ItemDTO;
import ooae_library.data_transfer_object.OrderDTO;
import ooae_library.data_transfer_object.SupplierDTO;
import ooae_library.keyboard_io.KeyboardInputter;

/**
 *
 * @author gdm1
 */
public class OOAE_Admin_Client
{

    private static final String HOSTNAME = "localhost";
    private static final int PORT_NUMBER = 30000;

    private static final int VIEW_ALL_ITEMS = 1;
    private static final int VIEW_ITEM = 2;
    private static final int ADD_ITEM = 3;
    private static final int VIEW_ITEMS_TO_REORDER = 4;
    private static final int SHIP_ORDER = 5;
    private static final int INITIALISE_DATABASE = 7;
    private static final int EXIT = 9;

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
                    case ADD_ITEM:
                        String name = KeyboardInputter.getString("Name");
                        String description = KeyboardInputter.getString("Description");
                        double price = KeyboardInputter.getDouble("Price");
                        int qtyInStock = KeyboardInputter.getInteger("Quantity in stock");
                        int stockReorderLevel = KeyboardInputter.getInteger("Stock re-order level");
                        int supplierId = KeyboardInputter.getInteger("Supplier id");

                        ItemDTO item2 = new ItemDTO(
                                description,
                                -1,
                                name,
                                price,
                                qtyInStock,
                                stockReorderLevel,
                                new SupplierDTO("", supplierId));

                        outToServer.println("Admin#addItem#" + new Gson().toJson(item2));

//                        System.out.println("Server says: " + inFromServer.readLine());
                        item2 = new Gson().fromJson(inFromServer.readLine(), ItemDTO.class);
                        if (item2 != null)
                        {
                            displayItem(item2);
                        }
                        break;

                    case INITIALISE_DATABASE:
                        outToServer.println("Admin#initialiseDatabase");
//                        System.out.println("Server says: " + inFromServer.readLine());
                        String serverResponse = inFromServer.readLine();
                        System.out.printf("\nDatabase has %sbeen initialised\n", serverResponse.equals("OK") ? "" : "not ");
                        break;

                    case SHIP_ORDER:
                        int orderId = KeyboardInputter.getInteger("Order id");

                        outToServer.println("Admin#shipOrder#" + new Gson().toJson(orderId));

//                        System.out.println("Server says: " + inFromServer.readLine());
                        OrderDTO shippedOrder = new Gson().fromJson(inFromServer.readLine(), OrderDTO.class);

                        System.out.printf("\nOrder %d has %sbeen shipped\n", orderId, shippedOrder != null ? "" : "not ");
                        break;

                    case VIEW_ALL_ITEMS:
                        outToServer.println("Admin#viewAllItems");

//                        System.out.println("Server says: " + inFromServer.readLine());
                        Type listType = new TypeToken<ArrayList<ItemDTO>>()
                        {
                        }.getType();
                        ArrayList<ItemDTO> list = new Gson().fromJson(inFromServer.readLine(), listType);

                        for (ItemDTO item : list)
                        {
                            displayItem(item);
                        }
                        break;

                    case VIEW_ITEM:
                        int itemId = KeyboardInputter.getInteger("Item id");

                        outToServer.println("Admin#viewItem#" + new Gson().toJson(itemId));

//                        System.out.println("Server says: " + inFromServer.readLine());
                        ItemDTO item = new Gson().fromJson(inFromServer.readLine(), ItemDTO.class);

                        if (item != null)
                        {
                            displayItem(item);

                            char response
                                    = KeyboardInputter
                                            .getString("\nUpdate quantity in stock? (y/n)")
                                            .toLowerCase()
                                            .charAt(0);

                            if (response == 'y')
                            {
                                socket = new Socket(HOSTNAME, PORT_NUMBER);
                                outToServer = new PrintWriter(socket.getOutputStream(), true);
                                inFromServer = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                                int qtyInStock1 = KeyboardInputter.getInteger("New quantity in stock");
                                item = new ItemDTO(
                                        item.getDescription(),
                                        item.getItemId(),
                                        item.getName(),
                                        item.getPrice(),
                                        qtyInStock1,
                                        item.getStockReorderLevel(),
                                        item.getSupplier());

                                outToServer.println("Admin#updateItemQuantityInStock#" + new Gson().toJson(item));

//                                System.out.println("Server says: " + inFromServer.readLine());
                                item = new Gson().fromJson(inFromServer.readLine(), ItemDTO.class);

                                if (item != null)
                                {
                                    displayItem(item);
                                }
                            }
                        }
                        break;

                    case VIEW_ITEMS_TO_REORDER:
                        outToServer.println("Admin#viewItemsToReorder");

//                        System.out.println("Server says: " + inFromServer.readLine());
                        listType = new TypeToken<ArrayList<ItemDTO>>()
                        {
                        }.getType();
                        list = new Gson().fromJson(inFromServer.readLine(), listType);

                        for (ItemDTO listItem : list)
                        {
                            displayItem(listItem);
                        }
                        break;
                    default:
                        break;
                }

                socket.close();
            }
            catch (IOException ioe)
            {
                System.out.println(ioe.getMessage());
            }
            choice = displayMenu();
        }
    }

    private static void displayItem(ItemDTO item)
    {
        System.out.println("\nId: " + item.getItemId());
        System.out.println("\tName: " + item.getName());
        System.out.println("\tDescription: " + item.getDescription());
        System.out.println("\tPrice: " + item.getPrice());
        System.out.println("\tQuantity in stock: " + item.getQuantityInStock());
        System.out.println("\tStock re-order level: " + item.getStockReorderLevel());
        System.out.println("\tSupplier: " + item.getSupplier().getName() + "(" + item.getSupplier().getSupplierId() + ")");
    }

    private static int displayMenu()
    {
        System.out.println("\nAdmin menu");
        System.out.println("1. View all items");
        System.out.println("2. View item");
        System.out.println("3. Add item");
        System.out.println("4. View items to re-order");
        System.out.println("5. Ship order");
        System.out.println("7. Initialise database");
        System.out.println("9. Exit");

        return KeyboardInputter.getInteger("\nOption");
    }
}
