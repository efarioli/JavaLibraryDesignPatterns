package ooae_customer_client;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.Calendar;
import java.util.HashMap;
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
public class OOAE_Customer_Client_Tests
{

    public OOAE_Customer_Client_Tests()
    {
    }

    @Test
    public void testDisplayOrderWithNoOrderLines()
    {
        //redirect System.out to an output stream
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        PrintStream ps = new PrintStream(outputStream);
        PrintStream old = System.out;
        System.setOut(ps);

        Calendar orderDateTime = Calendar.getInstance();
        orderDateTime.set(Calendar.YEAR, 2019);
        orderDateTime.set(Calendar.MONTH, 7);
        orderDateTime.set(Calendar.DAY_OF_MONTH, 23);
        orderDateTime.set(Calendar.HOUR_OF_DAY, 13);
        orderDateTime.set(Calendar.MINUTE, 10);
        orderDateTime.set(Calendar.SECOND, 34);
        //test
        OrderDTO order 
                = new OrderDTO(
                        null, 
                        orderDateTime, 
                        23, 
                        "Placed");

        OOAE_Customer_Client.displayOrder(order);
        
        //reset System.out to console
        System.out.flush();
        System.setOut(old);

        String expectedOutput
                = "\nOrder ID: 23 (Placed)\n"
                + "\tDate/time of order: 23/08/2019 13:10\r\n"
                + "\tItems\r\n"
                + "\t    ID    Name                     Price     Qty        Cost\n"
                + "\t------------------------------------------------------------\n"
                + "\tOrder total (£):                                        0.00\n";
        
        assertEquals(
                expectedOutput, 
                outputStream.toString());
    }

    @Test
    public void testDisplayOrderWithOneOrderLine()
    {
        //redirect System.out to an output stream
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        PrintStream ps = new PrintStream(outputStream);
        PrintStream old = System.out;
        System.setOut(ps);

        Calendar orderDateTime = Calendar.getInstance();
        orderDateTime.set(Calendar.YEAR, 2019);
        orderDateTime.set(Calendar.MONTH, 7);
        orderDateTime.set(Calendar.DAY_OF_MONTH, 23);
        orderDateTime.set(Calendar.HOUR_OF_DAY, 13);
        orderDateTime.set(Calendar.MINUTE, 10);
        orderDateTime.set(Calendar.SECOND, 34);
        //test
        OrderDTO order 
                = new OrderDTO(
                        null, 
                        orderDateTime, 
                        23, 
                        "Cancelled");
        
        ItemDTO item 
                = new ItemDTO(
                        "Item 43 description", 
                        45, 
                        "Item 43", 
                        0, 
                        0, 
                        0, 
                        new SupplierDTO("", 12));
        
        OrderLineDTO orderLine 
                = new OrderLineDTO(
                        item, 
                        order, 
                        1, 
                        21.05, 
                        3);
        
        HashMap<Integer, OrderLineDTO> lines = new HashMap<>(1);
        lines.put(orderLine.getOrderLineId(), orderLine);
        order.setOrderLines(lines);

        OOAE_Customer_Client.displayOrder(order);
        
        //reset System.out to console
        System.out.flush();
        System.setOut(old);

        String expectedOutput
                = "\nOrder ID: 23 (Cancelled)\n"
                + "\tDate/time of order: 23/08/2019 13:10\r\n"
                + "\tItems\r\n"
                + "\t    ID    Name                     Price     Qty        Cost\n"
                + "\t    45    Item 43                  21.05       3       63.15\n"
                + "\t------------------------------------------------------------\n"
                + "\tOrder total (£):                                       63.15\n";
        
        assertEquals(
                expectedOutput, 
                outputStream.toString());
    }

    @Test
    public void testDisplayOrderWithManyOrderLines()
    {
        //redirect System.out to an output stream
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        PrintStream ps = new PrintStream(outputStream);
        PrintStream old = System.out;
        System.setOut(ps);

        Calendar orderDateTime = Calendar.getInstance();
        orderDateTime.set(Calendar.YEAR, 2019);
        orderDateTime.set(Calendar.MONTH, 7);
        orderDateTime.set(Calendar.DAY_OF_MONTH, 23);
        orderDateTime.set(Calendar.HOUR_OF_DAY, 13);
        orderDateTime.set(Calendar.MINUTE, 10);
        orderDateTime.set(Calendar.SECOND, 34);
        //test
        OrderDTO order 
                = new OrderDTO(
                        null, 
                        orderDateTime, 
                        23, 
                        "Shipped");
        
        ItemDTO item1 
                = new ItemDTO(
                        "Item 43 description", 
                        43, 
                        "Item 43", 
                        0, 
                        0, 
                        0, 
                        new SupplierDTO("", 12));
        
        OrderLineDTO orderLine1 
                = new OrderLineDTO(
                        item1, 
                        order, 
                        1, 
                        21.05, 
                        3);
        
        ItemDTO item2 
                = new ItemDTO(
                        "Item 44 description", 
                        44, 
                        "Item 44", 
                        0, 
                        0, 
                        0, 
                        new SupplierDTO("", 13));
        
        OrderLineDTO orderLine2 
                = new OrderLineDTO(
                        item2, 
                        order, 
                        2, 
                        1.23, 
                        2);
        
        ItemDTO item3 
                = new ItemDTO(
                        "Item 45 description", 
                        45, 
                        "Item 45", 
                        0, 
                        0, 
                        0, 
                        new SupplierDTO("", 15));
        
        OrderLineDTO orderLine3 
                = new OrderLineDTO(
                        item3, 
                        order, 
                        3, 
                        6.20, 
                        5);
        
        HashMap<Integer, OrderLineDTO> lines = new HashMap<>(1);
        lines.put(orderLine1.getOrderLineId(), orderLine1);
        lines.put(orderLine2.getOrderLineId(), orderLine2);
        lines.put(orderLine3.getOrderLineId(), orderLine3);
        order.setOrderLines(lines);

        OOAE_Customer_Client.displayOrder(order);
        
        //reset System.out to console
        System.out.flush();
        System.setOut(old);

        String expectedOutput
                = "\nOrder ID: 23 (Shipped)\n"
                + "\tDate/time of order: 23/08/2019 13:10\r\n"
                + "\tItems\r\n"
                + "\t    ID    Name                     Price     Qty        Cost\n"
                + "\t    43    Item 43                  21.05       3       63.15\n"
                + "\t    44    Item 44                   1.23       2        2.46\n"
                + "\t    45    Item 45                   6.20       5       31.00\n"
                + "\t------------------------------------------------------------\n"
                + "\tOrder total (£):                                       96.61\n";
        
        assertEquals(
                expectedOutput, 
                outputStream.toString());
    }
}
