package ooae_server;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import ooae_library.data_transfer_object.ItemDTO;
import ooae_library.data_transfer_object.OrderDTO;
import ooae_library.data_transfer_object.OrderLineDTO;
import ooae_library.data_transfer_object.SupplierDTO;
import ooae_server.database.CustomerGateway;
import ooae_server.database.ItemGateway;
import ooae_server.database.OrderGateway;
import ooae_server.database.SupplierGateway;
import static org.junit.Assert.*;
import org.junit.Test;

/**
 *
 * @author gdm1
 */
public class AdminClientResponderTest
{

    public AdminClientResponderTest()
    {
    }

    @Test
    public void testAddItem()
    {
        //initialise database
        StringWriter outStr = new StringWriter();
        PrintWriter clientOut = new PrintWriter(outStr);
        String[] inputParts = "Admin#initialseDatabase".split("#");
        AdminClientResponder responder = new AdminClientResponder(null, clientOut, inputParts);
        responder.run();

        //test
        outStr = new StringWriter();
        clientOut = new PrintWriter(outStr);
        ItemDTO itemToAdd = new ItemDTO(
                "Item 11 description",
                11,
                "Item 11",
                11.1,
                12,
                4,
                new SupplierDTO("", 1));
        inputParts = ("Admin#addItem#" + new Gson().toJson(itemToAdd)).split("#");
        responder = new AdminClientResponder(null, clientOut, inputParts);
        responder.run();

        ItemDTO addedItem = new Gson().fromJson(outStr.toString(), ItemDTO.class);
        ItemDTO expectedItem = new ItemDTO("Item 11 description", 11, "Item 11", 11.1, 12, 4, new SupplierDTO("Supplier 1", 1));

        assertEquals(
                expectedItem,
                addedItem);
    }

    @Test
    public void testCheckNeedForDatabaseInitialisation() throws Exception
    {
        //initialise database
        new OrderGateway().dropTable();
        new ItemGateway().dropTable();
        new SupplierGateway().dropTable();
        new CustomerGateway().dropTable();

        //test
        StringWriter outStr = new StringWriter();
        PrintWriter clientOut = new PrintWriter(outStr);
        String[] inputParts = "Admin#checkNeedForDatabaseInitialisation".split("#");
        AdminClientResponder responder = new AdminClientResponder(null, clientOut, inputParts);
        responder.run();

        assertEquals(
                "OK\r\n",
                outStr.toString());
    }

    @Test
    public void testIncorrectCommand()
    {
        StringWriter outStr = new StringWriter();
        PrintWriter clientOut = new PrintWriter(outStr);
        String[] inputParts = "Admin#unknownCommand".split("#");
        AdminClientResponder responder = new AdminClientResponder(null, clientOut, inputParts);
        responder.run();

        assertEquals(
                "ERROR: Unrecognised command\r\n",
                outStr.toString());
    }

    @Test
    public void testInitialiseDatabase()
    {
        StringWriter outStr = new StringWriter();
        PrintWriter clientOut = new PrintWriter(outStr);
        String[] inputParts = "Admin#initialiseDatabase".split("#");
        AdminClientResponder responder = new AdminClientResponder(null, clientOut, inputParts);
        responder.run();

        assertEquals(
                "OK\r\n",
                outStr.toString());
    }

    @Test
    public void testShipOrder()
    {
        //initialise database
        StringWriter outStr = new StringWriter();
        PrintWriter clientOut = new PrintWriter(outStr);
        String[] inputParts = "Admin#initialiseDatabase".split("#");
        AdminClientResponder responder = new AdminClientResponder(null, clientOut, inputParts);
        responder.run();

        //test
        outStr = new StringWriter();
        clientOut = new PrintWriter(outStr);
        inputParts = "Admin#shipOrder#13".split("#");
        responder = new AdminClientResponder(null, clientOut, inputParts);
        responder.run();

        OrderDTO order = new Gson().fromJson(outStr.toString(), OrderDTO.class);
        OrderDTO expectedOrder = new OrderDTO(null, order.getOrderDateTime(), 13, "Shipped");
        HashMap<Integer, OrderLineDTO> lines = new HashMap<>();
        lines.put(
                44,
                new OrderLineDTO(
                        new ItemDTO("Description of item 3", 3, "Item 3", 0, 0, 0, null),
                        expectedOrder,
                        44,
                        13.25,
                        2));
        lines.put(
                43,
                new OrderLineDTO(
                        new ItemDTO("Description of item 2", 2, "Item 2", 0, 0, 0, null),
                        expectedOrder,
                        43,
                        13.15,
                        1));
        expectedOrder.setOrderLines(lines);

        assertEquals(
                expectedOrder,
                order);
    }

    @Test
    public void testViewAllItems()
    {
        //initialise database
        StringWriter outStr = new StringWriter();
        PrintWriter clientOut = new PrintWriter(outStr);
        String[] inputParts = "Admin#initialiseDatabase".split("#");
        AdminClientResponder responder = new AdminClientResponder(null, clientOut, inputParts);
        responder.run();

        //test
        outStr = new StringWriter();
        clientOut = new PrintWriter(outStr);
        inputParts = "Admin#viewAllItems".split("#");
        responder = new AdminClientResponder(null, clientOut, inputParts);
        responder.run();

        Type listType = new TypeToken<ArrayList<ItemDTO>>()
        {
        }.getType();
        ArrayList<ItemDTO> items = new Gson().fromJson(outStr.toString(), listType);
        ArrayList<ItemDTO> expectedItems = new ArrayList<>(10);
        expectedItems.add(new ItemDTO("Description of item 1", 1, "Item 1", 1.15, 5, 5, new SupplierDTO("Supplier 2", 2)));
        expectedItems.add(new ItemDTO("Description of item 2", 2, "Item 2", 2.25, 6, 5, new SupplierDTO("Supplier 3", 3)));
        expectedItems.add(new ItemDTO("Description of item 3", 3, "Item 3", 3.35, 7, 5, new SupplierDTO("Supplier 4", 4)));
        expectedItems.add(new ItemDTO("Description of item 4", 4, "Item 4", 4.45, 8, 5, new SupplierDTO("Supplier 5", 5)));
        expectedItems.add(new ItemDTO("Description of item 5", 5, "Item 5", 5.55, 4, 5, new SupplierDTO("Supplier 1", 1)));
        expectedItems.add(new ItemDTO("Description of item 6", 6, "Item 6", 6.65, 5, 5, new SupplierDTO("Supplier 2", 2)));
        expectedItems.add(new ItemDTO("Description of item 7", 7, "Item 7", 7.75, 6, 5, new SupplierDTO("Supplier 3", 3)));
        expectedItems.add(new ItemDTO("Description of item 8", 8, "Item 8", 8.85, 7, 5, new SupplierDTO("Supplier 4", 4)));
        expectedItems.add(new ItemDTO("Description of item 9", 9, "Item 9", 9.95, 8, 5, new SupplierDTO("Supplier 5", 5)));
        expectedItems.add(new ItemDTO("Description of item 10", 10, "Item 10", 10.1, 4, 5, new SupplierDTO("Supplier 1", 1)));

        assertEquals(
                expectedItems,
                items);
    }

    @Test
    public void testViewItem()
    {
        //initialise database
        StringWriter outStr = new StringWriter();
        PrintWriter clientOut = new PrintWriter(outStr);
        String[] inputParts = "Admin#initialiseDatabase".split("#");
        AdminClientResponder responder = new AdminClientResponder(null, clientOut, inputParts);
        responder.run();

        //test
        outStr = new StringWriter();
        clientOut = new PrintWriter(outStr);
        inputParts = "Admin#viewItem#10".split("#");
        responder = new AdminClientResponder(null, clientOut, inputParts);
        responder.run();

        ItemDTO item = new Gson().fromJson(outStr.toString(), ItemDTO.class);
        ItemDTO expectedItem = new ItemDTO("Description of item 10", 10, "Item 10", 10.1, 4, 5, new SupplierDTO("Supplier 1", 1));

        assertEquals(
                expectedItem,
                item);
    }

    @Test
    public void testViewItemsToReorder()
    {
        //initialise database
        StringWriter outStr = new StringWriter();
        PrintWriter clientOut = new PrintWriter(outStr);
        String[] inputParts = "Admin#initialiseDatabase".split("#");
        AdminClientResponder responder = new AdminClientResponder(null, clientOut, inputParts);
        responder.run();

        //test
        outStr = new StringWriter();
        clientOut = new PrintWriter(outStr);
        inputParts = "Admin#viewItemsToReorder".split("#");
        responder = new AdminClientResponder(null, clientOut, inputParts);
        responder.run();

        Type listType = new TypeToken<ArrayList<ItemDTO>>()
        {
        }.getType();
        ArrayList<ItemDTO> items = new Gson().fromJson(outStr.toString(), listType);
        ArrayList<ItemDTO> expectedItems = new ArrayList<>(4);
        expectedItems.add(new ItemDTO("Description of item 1", 1, "Item 1", 1.15, 5, 5, new SupplierDTO("Supplier 2", 2)));
        expectedItems.add(new ItemDTO("Description of item 5", 5, "Item 5", 5.55, 4, 5, new SupplierDTO("Supplier 1", 1)));
        expectedItems.add(new ItemDTO("Description of item 6", 6, "Item 6", 6.65, 5, 5, new SupplierDTO("Supplier 2", 2)));
        expectedItems.add(new ItemDTO("Description of item 10", 10, "Item 10", 10.1, 4, 5, new SupplierDTO("Supplier 1", 1)));

        assertEquals(
                expectedItems,
                items);
    }
}
