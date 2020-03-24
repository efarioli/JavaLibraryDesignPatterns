/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ooae_customer_client.visitor;

import java.text.SimpleDateFormat;
import ooae_library.data_transfer_object.OrderLineDTO;

/**
 *
 * @author f023507i
 */
public class FormatOrderAsString implements Visitor
{

    private static final SimpleDateFormat SDF = new SimpleDateFormat("dd/MM/yyyy HH:mm");

    @Override
    public String visit(OrderDTOVisitable order)
    {
        double orderTotal = 0;
        String res = "";
        res = res + String.format("\nOrder ID: %d (%s)\n", order.getOrderId(), order.getStatus());
        res = res + String.format("\tDate/time of order: " + SDF.format(order.getOrderDateTime().getTime()) + "\n");
        res = res + String.format("\tItems\n");
        res = res + String.format("\t    %-4s  %-20s    %6s    %4s    %8s\n", "ID", "Name", "Price", "Qty", "Cost");
        for (OrderLineDTO line : order.getOrderLines().values())
        {
            orderTotal += line.getPrice() * line.getQuantity();
            res = res + String.format("\t    %-4d  %-20s    %6.2f    %4d    %8.2f\n",
                    line.getItem().getItemId(),
                    line.getItem().getName(),
                    line.getPrice(),
                    line.getQuantity(),
                    line.getPrice() * line.getQuantity());
        }
        res = res + String.format("\t------------------------------------------------------------\n");
        res = res + String.format("\tOrder total (£):                                    %8.2f\n", orderTotal);
        return res;
    }

}
