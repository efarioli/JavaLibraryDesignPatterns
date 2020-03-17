package ooae_server;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import ooae_library.data_transfer_object.CustomerDTO;
import ooae_library.data_transfer_object.ItemDTO;
import ooae_library.data_transfer_object.OrderDTO;
import ooae_library.data_transfer_object.OrderLineDTO;
import ooae_library.data_transfer_object.SupplierDTO;
import static org.junit.Assert.*;
import org.junit.Test;

/**
 *
 * @author gdm1
 */
public class CustomerClientResponderTest
{

    public CustomerClientResponderTest()
    {
    }

    @Test
    public void testCancelCancelledOrder()
    {
        //initialise database
        StringWriter outStr = new StringWriter();
        PrintWriter clientOut = new PrintWriter(outStr);
        String[] inputParts = "Admin#initialiseDatabase".split("#");
        AdminClientResponder adminResponder = new AdminClientResponder(null, clientOut, inputParts);
        adminResponder.run();

        //test
        outStr = new StringWriter();
        clientOut = new PrintWriter(outStr);
        inputParts = "Customer#cancelOrder#6".split("#");
        CustomerClientResponder customerResponder = new CustomerClientResponder(null, clientOut, inputParts);
        customerResponder.run();

        OrderDTO order = new Gson().fromJson(outStr.toString(), OrderDTO.class);

        assertNull(order);
    }

    @Test
    public void testCancelPlacedOrder()
    {
        //initialise database
        StringWriter outStr = new StringWriter();
        PrintWriter clientOut = new PrintWriter(outStr);
        String[] inputParts = "Admin#initialiseDatabase".split("#");
        AdminClientResponder adminResponder = new AdminClientResponder(null, clientOut, inputParts);
        adminResponder.run();

        //test
        outStr = new StringWriter();
        clientOut = new PrintWriter(outStr);
        inputParts = "Customer#cancelOrder#13".split("#");
        CustomerClientResponder customerResponder = new CustomerClientResponder(null, clientOut, inputParts);
        customerResponder.run();

        OrderDTO order = new Gson().fromJson(outStr.toString(), OrderDTO.class);
        OrderDTO expectedOrder = new OrderDTO(null, order.getOrderDateTime(), 13, "Cancelled");
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
    public void testCancelShippedOrder()
    {
        //initialise database
        StringWriter outStr = new StringWriter();
        PrintWriter clientOut = new PrintWriter(outStr);
        String[] inputParts = "Admin#initialiseDatabase".split("#");
        AdminClientResponder adminResponder = new AdminClientResponder(null, clientOut, inputParts);
        adminResponder.run();

        //test
        outStr = new StringWriter();
        clientOut = new PrintWriter(outStr);
        inputParts = "Customer#cancelOrder#15".split("#");
        CustomerClientResponder customerResponder = new CustomerClientResponder(null, clientOut, inputParts);
        customerResponder.run();

        OrderDTO order = new Gson().fromJson(outStr.toString(), OrderDTO.class);

        assertNull(order);
    }

    @Test
    public void testIncorrectCommand()
    {
        StringWriter outStr = new StringWriter();
        PrintWriter clientOut = new PrintWriter(outStr);
        String[] inputParts = "Customer#unknownCommand".split("#");
        CustomerClientResponder customerResponder = new CustomerClientResponder(null, clientOut, inputParts);
        customerResponder.run();

        assertEquals(
                "ERROR: Unrecognised command\r\n",
                outStr.toString());
    }

    @Test
    public void testLoginWithCorrectCredentials()
    {
        //initialise database
        StringWriter outStr = new StringWriter();
        PrintWriter clientOut = new PrintWriter(outStr);
        String[] inputParts = "Admin#initialiseDatabase".split("#");
        AdminClientResponder adminResponder = new AdminClientResponder(null, clientOut, inputParts);
        adminResponder.run();

        //test
        outStr = new StringWriter();
        clientOut = new PrintWriter(outStr);
        CustomerDTO credentials
                = new CustomerDTO(
                        -1,
                        "",
                        "Customer1",
                        "Customer1");

        inputParts = ("Customer#login#" + new Gson().toJson(credentials)).split("#");
        CustomerClientResponder customerResponder = new CustomerClientResponder(null, clientOut, inputParts);
        customerResponder.run();

        CustomerDTO user = new Gson().fromJson(outStr.toString(), CustomerDTO.class);
        CustomerDTO expectedUser
                = new CustomerDTO(
                        1,
                        "Customer 1",
                        "",
                        "Customer1");

        assertEquals(
                expectedUser,
                user);
    }

    @Test
    public void testLoginWithIncorrectPassword()
    {
        //initialise database
        StringWriter outStr = new StringWriter();
        PrintWriter clientOut = new PrintWriter(outStr);
        String[] inputParts = "Admin#initialiseDatabase".split("#");
        AdminClientResponder adminResponder = new AdminClientResponder(null, clientOut, inputParts);
        adminResponder.run();

        //test
        outStr = new StringWriter();
        clientOut = new PrintWriter(outStr);
        CustomerDTO credentials
                = new CustomerDTO(
                        -1,
                        "",
                        "Customer1",
                        "abc");

        inputParts = ("Customer#login#" + new Gson().toJson(credentials)).split("#");
        CustomerClientResponder customerResponder = new CustomerClientResponder(null, clientOut, inputParts);
        customerResponder.run();

        CustomerDTO user = new Gson().fromJson(outStr.toString(), CustomerDTO.class);
        CustomerDTO expectedUser
                = new CustomerDTO(
                        -1,
                        null,
                        "",
                        null);

        assertEquals(
                expectedUser,
                user);
    }

    @Test
    public void testLoginWithIncorrectUsername()
    {
        //initialise database
        StringWriter outStr = new StringWriter();
        PrintWriter clientOut = new PrintWriter(outStr);
        String[] inputParts = "Admin#initialiseDatabase".split("#");
        AdminClientResponder adminResponder = new AdminClientResponder(null, clientOut, inputParts);
        adminResponder.run();

        //test
        outStr = new StringWriter();
        clientOut = new PrintWriter(outStr);
        CustomerDTO credentials
                = new CustomerDTO(
                        -1,
                        "",
                        "abc",
                        "Customer1");

        inputParts = ("Customer#login#" + new Gson().toJson(credentials)).split("#");
        CustomerClientResponder customerResponder = new CustomerClientResponder(null, clientOut, inputParts);
        customerResponder.run();

        CustomerDTO user = new Gson().fromJson(outStr.toString(), CustomerDTO.class);
        CustomerDTO expectedUser
                = new CustomerDTO(
                        -1,
                        null,
                        "",
                        null);

        assertEquals(
                expectedUser,
                user);
    }

    @Test
    public void testPlaceOrder()
    {
        //initialise database
        StringWriter outStr = new StringWriter();
        PrintWriter clientOut = new PrintWriter(outStr);
        String[] inputParts = "Admin#initialiseDatabase".split("#");
        AdminClientResponder adminResponder = new AdminClientResponder(null, clientOut, inputParts);
        adminResponder.run();

        //test
        outStr = new StringWriter();
        clientOut = new PrintWriter(outStr);
        CustomerDTO customer3 = new CustomerDTO(3, "", "", "");
        Calendar orderToPlaceDataTime = Calendar.getInstance();
        orderToPlaceDataTime.set(Calendar.YEAR, 2019);
        orderToPlaceDataTime.set(Calendar.MONTH, 10);
        orderToPlaceDataTime.set(Calendar.DAY_OF_MONTH, 12);
        orderToPlaceDataTime.set(Calendar.HOUR_OF_DAY, 13);
        orderToPlaceDataTime.set(Calendar.MINUTE, 53);
        orderToPlaceDataTime.set(Calendar.SECOND, 45);
        OrderDTO orderToPlace = new OrderDTO(customer3, orderToPlaceDataTime, -1, "");
        HashMap<Integer, OrderLineDTO> orderToPlaceLines = new HashMap<>();
        orderToPlaceLines.put(
                1,
                new OrderLineDTO(
                        new ItemDTO("Description of item 3", 3, "Item 3", 0, 0, 0, new SupplierDTO("", 4)),
                        orderToPlace,
                        1,
                        2.25,
                        2));
        orderToPlace.setOrderLines(orderToPlaceLines);
        HashMap<Integer, OrderDTO> orders = new HashMap<>();
        orders.put(orderToPlace.getOrderId(), orderToPlace);
        customer3.setOrders(orders);

        inputParts = ("Customer#placeOrder#" + new Gson().toJson(customer3)).split("#");
        CustomerClientResponder customerResponder = new CustomerClientResponder(null, clientOut, inputParts);
        customerResponder.run();

        OrderDTO placedOrder = new Gson().fromJson(outStr.toString(), OrderDTO.class);

        Calendar expectedOrderDataTime = Calendar.getInstance();
        expectedOrderDataTime.set(Calendar.YEAR, 2019);
        expectedOrderDataTime.set(Calendar.MONTH, 10);
        expectedOrderDataTime.set(Calendar.DAY_OF_MONTH, 12);
        expectedOrderDataTime.set(Calendar.HOUR_OF_DAY, 13);
        expectedOrderDataTime.set(Calendar.MINUTE, 53);
        expectedOrderDataTime.set(Calendar.SECOND, 45);
        OrderDTO expectedOrder = new OrderDTO(null, expectedOrderDataTime, 21, "Placed");
        HashMap<Integer, OrderLineDTO> expectedOrderLines = new HashMap<>();
        expectedOrderLines.put(
                69,
                new OrderLineDTO(
                        new ItemDTO("Description of item 3", 3, "Item 3", 0, 0, 0, null),
                        expectedOrder,
                        69,
                        2.25,
                        2));
        expectedOrder.setOrderLines(expectedOrderLines);

        assertEquals(
                expectedOrder,
                placedOrder);
    }

    @Test
    public void testViewAllItems()
    {
        //initialise database
        StringWriter outStr = new StringWriter();
        PrintWriter clientOut = new PrintWriter(outStr);
        String[] inputParts = "Admin#initialiseDatabase".split("#");
        AdminClientResponder adminResponder = new AdminClientResponder(null, clientOut, inputParts);
        adminResponder.run();

        //test
        outStr = new StringWriter();
        clientOut = new PrintWriter(outStr);
        inputParts = "Customer#viewAllItems".split("#");
        CustomerClientResponder customerResponder = new CustomerClientResponder(null, clientOut, inputParts);
        customerResponder.run();

        Type listType = new TypeToken<ArrayList<ItemDTO>>()
        {
        }.getType();
        ArrayList<ItemDTO> items = new Gson().fromJson(outStr.toString(), listType);
        ArrayList<ItemDTO> expectedItems = new ArrayList<>(4);
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
    public void testViewExistingItem()
    {
        //initialise database
        StringWriter outStr = new StringWriter();
        PrintWriter clientOut = new PrintWriter(outStr);
        String[] inputParts = "Admin#initialiseDatabase".split("#");
        AdminClientResponder adminResponder = new AdminClientResponder(null, clientOut, inputParts);
        adminResponder.run();

        //test
        outStr = new StringWriter();
        clientOut = new PrintWriter(outStr);
        inputParts = "Admin#viewItem#10".split("#");
        CustomerClientResponder customerResponder = new CustomerClientResponder(null, clientOut, inputParts);
        customerResponder.run();

        ItemDTO item = new Gson().fromJson(outStr.toString(), ItemDTO.class);
        ItemDTO expectedItem = new ItemDTO("Description of item 10", 10, "Item 10", 10.1, 4, 5, new SupplierDTO("Supplier 1", 1));

        assertEquals(
                expectedItem,
                item);
    }

    @Test
    public void testViewNonExistantItem()
    {
        //initialise database
        StringWriter outStr = new StringWriter();
        PrintWriter clientOut = new PrintWriter(outStr);
        String[] inputParts = "Admin#initialiseDatabase".split("#");
        AdminClientResponder adminResponder = new AdminClientResponder(null, clientOut, inputParts);
        adminResponder.run();

        //test
        outStr = new StringWriter();
        clientOut = new PrintWriter(outStr);
        inputParts = "Admin#viewItem#11".split("#");
        CustomerClientResponder customerResponder = new CustomerClientResponder(null, clientOut, inputParts);
        customerResponder.run();

        ItemDTO item = new Gson().fromJson(outStr.toString(), ItemDTO.class);
        
        assertNull(item);
    }

    @Test
    public void testViewMyOrders()
    {
        //initialise database
        StringWriter outStr = new StringWriter();
        PrintWriter clientOut = new PrintWriter(outStr);
        String[] inputParts = "Admin#initialiseDatabase".split("#");
        AdminClientResponder adminResponder = new AdminClientResponder(null, clientOut, inputParts);
        adminResponder.run();

        //test
        outStr = new StringWriter();
        clientOut = new PrintWriter(outStr);
        CustomerDTO customer3 = new CustomerDTO(3, "", "", "");
        inputParts = ("Customer#viewMyOrders#" + new Gson().toJson(customer3)).split("#");
        CustomerClientResponder customerResponder = new CustomerClientResponder(null, clientOut, inputParts);
        customerResponder.run();

        Type mapType = new TypeToken<HashMap<Integer, OrderDTO>>()
        {
        }.getType();
        HashMap<Integer, OrderDTO> ordersForCustomer3 = new Gson().fromJson(outStr.toString(), mapType);

        HashMap<Integer, OrderDTO> expectedOrders = new HashMap<>();

        Calendar order2DataTime = Calendar.getInstance();
        order2DataTime.set(Calendar.YEAR, 2019);
        order2DataTime.set(Calendar.MONTH, 7);
        order2DataTime.set(Calendar.DAY_OF_MONTH, 23);
        order2DataTime.set(Calendar.HOUR_OF_DAY, 13);
        order2DataTime.set(Calendar.MINUTE, 53);
        order2DataTime.set(Calendar.SECOND, 45);
        OrderDTO order2 = new OrderDTO(null, order2DataTime, 2, "Placed");
        HashMap<Integer, OrderLineDTO> order2Lines = new HashMap<>();
        order2Lines.put(
                4,
                new OrderLineDTO(
                        new ItemDTO("Description of item 3", 3, "Item 3", 0, 0, 0, null),
                        order2,
                        4,
                        2.25,
                        2));
        order2Lines.put(
                5,
                new OrderLineDTO(
                        new ItemDTO("Description of item 4", 4, "Item 4", 0, 0, 0, null),
                        order2,
                        5,
                        2.35,
                        3));
        order2Lines.put(
                3,
                new OrderLineDTO(
                        new ItemDTO("Description of item 2", 2, "Item 2", 0, 0, 0, null),
                        order2,
                        3,
                        2.15,
                        1));
        order2.setOrderLines(order2Lines);
        expectedOrders.put(2, order2);

        Calendar order12DataTime = Calendar.getInstance();
        order12DataTime.set(Calendar.YEAR, 2019);
        order12DataTime.set(Calendar.MONTH, 7);
        order12DataTime.set(Calendar.DAY_OF_MONTH, 23);
        order12DataTime.set(Calendar.HOUR_OF_DAY, 13);
        order12DataTime.set(Calendar.MINUTE, 53);
        order12DataTime.set(Calendar.SECOND, 45);
        OrderDTO order12 = new OrderDTO(null, order12DataTime, 12, "Cancelled");
        HashMap<Integer, OrderLineDTO> order12Lines = new HashMap<>();
        order12Lines.put(
                42,
                new OrderLineDTO(
                        new ItemDTO("Description of item 2", 2, "Item 2", 0, 0, 0, null),
                        order12,
                        42,
                        12.15,
                        1));
        order12.setOrderLines(order12Lines);
        expectedOrders.put(12, order12);

        assertEquals(
                expectedOrders,
                ordersForCustomer3);
    }
}
