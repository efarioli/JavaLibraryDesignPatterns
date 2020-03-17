package ooae_server.database;

import java.io.PrintWriter;
import java.io.StringWriter;
import ooae_server.AdminClientResponder;
import static org.junit.Assert.*;
import org.junit.Test;

/**
 *
 * @author gdm1
 */
public class GatewayTest
{

    public GatewayTest()
    {
    }

    @Test
    public void testDropCustomerTable() throws Exception
    {
        //initialise database
        StringWriter outStr = new StringWriter();
        PrintWriter clientOut = new PrintWriter(outStr);
        String[] inputParts = "Admin#initialiseDatabase".split("#");
        AdminClientResponder responder = new AdminClientResponder(null, clientOut, inputParts);
        responder.run();

        //test
        new OrderGateway().dropTable();
        new ItemGateway().dropTable();
        CustomerGateway customerTable = new CustomerGateway();
        customerTable.dropTable();

        assertFalse(customerTable.exists());
    }

    @Test
    public void testDropItemTable() throws Exception
    {
        //initialise database
        StringWriter outStr = new StringWriter();
        PrintWriter clientOut = new PrintWriter(outStr);
        String[] inputParts = "Admin#initialiseDatabase".split("#");
        AdminClientResponder responder = new AdminClientResponder(null, clientOut, inputParts);
        responder.run();

        //test
        new OrderGateway().dropTable();
        ItemGateway itemTable = new ItemGateway();
        itemTable.dropTable();
        new SupplierGateway().dropTable();

        assertFalse(itemTable.exists());
    }

    @Test
    public void testDropOrderTable() throws Exception
    {
        //initialise database
        StringWriter outStr = new StringWriter();
        PrintWriter clientOut = new PrintWriter(outStr);
        String[] inputParts = "Admin#initialiseDatabase".split("#");
        AdminClientResponder responder = new AdminClientResponder(null, clientOut, inputParts);
        responder.run();

        //test
        OrderGateway orderTable = new OrderGateway();
        orderTable.dropTable();

        assertFalse(orderTable.exists());
    }

    @Test
    public void testDropSupplierTable() throws Exception
    {
        //initialise database
        StringWriter outStr = new StringWriter();
        PrintWriter clientOut = new PrintWriter(outStr);
        String[] inputParts = "Admin#initialiseDatabase".split("#");
        AdminClientResponder responder = new AdminClientResponder(null, clientOut, inputParts);
        responder.run();

        //test
        new OrderGateway().dropTable();
        new ItemGateway().dropTable();
        SupplierGateway supplierTable = new SupplierGateway();
        supplierTable.dropTable();

        assertFalse(supplierTable.exists());
    }

    @Test
    public void testInitialiseCustomerTable() throws Exception
    {
        //initialise database
        StringWriter outStr = new StringWriter();
        PrintWriter clientOut = new PrintWriter(outStr);
        String[] inputParts = "Admin#initialiseDatabase".split("#");
        AdminClientResponder responder = new AdminClientResponder(null, clientOut, inputParts);
        responder.run();

        //test
        new OrderGateway().dropTable();
        new ItemGateway().dropTable();
        CustomerGateway customerTable = new CustomerGateway();
        customerTable.dropTable();
        
        customerTable.initialiseTable();

        assertTrue(customerTable.exists());
    }

    @Test
    public void testInitialiseItemTable() throws Exception
    {
        //initialise database
        StringWriter outStr = new StringWriter();
        PrintWriter clientOut = new PrintWriter(outStr);
        String[] inputParts = "Admin#initialiseDatabase".split("#");
        AdminClientResponder responder = new AdminClientResponder(null, clientOut, inputParts);
        responder.run();

        //test
        new OrderGateway().dropTable();
        ItemGateway itemTable = new ItemGateway();
        itemTable.dropTable();
        SupplierGateway supplierTable = new SupplierGateway();
        supplierTable.dropTable();

        supplierTable.initialiseTable();
        itemTable.initialiseTable();

        assertTrue(itemTable.exists());
    }

    @Test
    public void testInitialiseOrderTable() throws Exception
    {
        //initialise database
        StringWriter outStr = new StringWriter();
        PrintWriter clientOut = new PrintWriter(outStr);
        String[] inputParts = "Admin#initialiseDatabase".split("#");
        AdminClientResponder responder = new AdminClientResponder(null, clientOut, inputParts);
        responder.run();

        //test
        OrderGateway orderTable = new OrderGateway();
        orderTable.dropTable();
        
        orderTable.initialiseTable();

        assertTrue(orderTable.exists());
    }

    @Test
    public void testInitialiseSupplierTable() throws Exception
    {
        //initialise database
        StringWriter outStr = new StringWriter();
        PrintWriter clientOut = new PrintWriter(outStr);
        String[] inputParts = "Admin#initialiseDatabase".split("#");
        AdminClientResponder responder = new AdminClientResponder(null, clientOut, inputParts);
        responder.run();

        //test
        new OrderGateway().dropTable();
        new ItemGateway().dropTable();
        SupplierGateway supplierTable = new SupplierGateway();
        supplierTable.dropTable();
        
        supplierTable.initialiseTable();

        assertTrue(supplierTable.exists());
    }
//
//    @Test
//    public void testViewAllItems()
//    {
//        //initialise database
//        StringWriter outStr = new StringWriter();
//        PrintWriter clientOut = new PrintWriter(outStr);
//        String[] inputParts = "Admin#initialiseDatabase".split("#");
//        AdminClientResponder responder = new AdminClientResponder(null, clientOut, inputParts);
//        responder.run();
//
//        //test
//        outStr = new StringWriter();
//        clientOut = new PrintWriter(outStr);
//        inputParts = "Admin#viewAllItems".split("#");
//        responder = new AdminClientResponder(null, clientOut, inputParts);
//        responder.run();
//
//        Type listType = new TypeToken<ArrayList<ItemDTO>>()
//        {
//        }.getType();
//        ArrayList<ItemDTO> items = new Gson().fromJson(outStr.toString(), listType);
//        ArrayList<ItemDTO> expectedItems = new ArrayList<>(10);
//        expectedItems.add(new ItemDTO("Description of item 1", 1, "Item 1", 1.15, 5, 5, new SupplierDTO("Supplier 2", 2)));
//        expectedItems.add(new ItemDTO("Description of item 2", 2, "Item 2", 2.25, 6, 5, new SupplierDTO("Supplier 3", 3)));
//        expectedItems.add(new ItemDTO("Description of item 3", 3, "Item 3", 3.35, 7, 5, new SupplierDTO("Supplier 4", 4)));
//        expectedItems.add(new ItemDTO("Description of item 4", 4, "Item 4", 4.45, 8, 5, new SupplierDTO("Supplier 5", 5)));
//        expectedItems.add(new ItemDTO("Description of item 5", 5, "Item 5", 5.55, 4, 5, new SupplierDTO("Supplier 1", 1)));
//        expectedItems.add(new ItemDTO("Description of item 6", 6, "Item 6", 6.65, 5, 5, new SupplierDTO("Supplier 2", 2)));
//        expectedItems.add(new ItemDTO("Description of item 7", 7, "Item 7", 7.75, 6, 5, new SupplierDTO("Supplier 3", 3)));
//        expectedItems.add(new ItemDTO("Description of item 8", 8, "Item 8", 8.85, 7, 5, new SupplierDTO("Supplier 4", 4)));
//        expectedItems.add(new ItemDTO("Description of item 9", 9, "Item 9", 9.95, 8, 5, new SupplierDTO("Supplier 5", 5)));
//        expectedItems.add(new ItemDTO("Description of item 10", 10, "Item 10", 10.1, 4, 5, new SupplierDTO("Supplier 1", 1)));
//
//        assertEquals(
//                expectedItems,
//                items);
//    }
//
//    @Test
//    public void testViewItem()
//    {
//        //initialise database
//        StringWriter outStr = new StringWriter();
//        PrintWriter clientOut = new PrintWriter(outStr);
//        String[] inputParts = "Admin#initialiseDatabase".split("#");
//        AdminClientResponder responder = new AdminClientResponder(null, clientOut, inputParts);
//        responder.run();
//
//        //test
//        outStr = new StringWriter();
//        clientOut = new PrintWriter(outStr);
//        inputParts = "Admin#viewItem#10".split("#");
//        responder = new AdminClientResponder(null, clientOut, inputParts);
//        responder.run();
//
//        ItemDTO item = new Gson().fromJson(outStr.toString(), ItemDTO.class);
//        ItemDTO expectedItem = new ItemDTO("Description of item 10", 10, "Item 10", 10.1, 4, 5, new SupplierDTO("Supplier 1", 1));
//
//        assertEquals(
//                expectedItem,
//                item);
//    }
//
//    @Test
//    public void testViewItemsToReorder()
//    {
//        //initialise database
//        StringWriter outStr = new StringWriter();
//        PrintWriter clientOut = new PrintWriter(outStr);
//        String[] inputParts = "Admin#initialiseDatabase".split("#");
//        AdminClientResponder responder = new AdminClientResponder(null, clientOut, inputParts);
//        responder.run();
//
//        //test
//        outStr = new StringWriter();
//        clientOut = new PrintWriter(outStr);
//        inputParts = "Admin#viewItemsToReorder".split("#");
//        responder = new AdminClientResponder(null, clientOut, inputParts);
//        responder.run();
//
//        Type listType = new TypeToken<ArrayList<ItemDTO>>()
//        {
//        }.getType();
//        ArrayList<ItemDTO> items = new Gson().fromJson(outStr.toString(), listType);
//        ArrayList<ItemDTO> expectedItems = new ArrayList<>(4);
//        expectedItems.add(new ItemDTO("Description of item 1", 1, "Item 1", 1.15, 5, 5, new SupplierDTO("Supplier 2", 2)));
//        expectedItems.add(new ItemDTO("Description of item 5", 5, "Item 5", 5.55, 4, 5, new SupplierDTO("Supplier 1", 1)));
//        expectedItems.add(new ItemDTO("Description of item 6", 6, "Item 6", 6.65, 5, 5, new SupplierDTO("Supplier 2", 2)));
//        expectedItems.add(new ItemDTO("Description of item 10", 10, "Item 10", 10.1, 4, 5, new SupplierDTO("Supplier 1", 1)));
//
//        assertEquals(
//                expectedItems,
//                items);
//    }
}
