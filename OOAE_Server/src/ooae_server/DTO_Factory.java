/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ooae_server;

import java.util.ArrayList;
import java.util.HashMap;
import ooae_library.data_transfer_object.*;
import ooae_server.entity.*;

/**
 *
 * @author f023507i
 */
public class DTO_Factory
{

    public static ItemDTO create(Item item)
    {
        ItemDTO itemDTO
                = new ItemDTO(
                        item.getDescription(),
                        item.getItemId(),
                        item.getName(),
                        item.getPrice(),
                        item.getQuantityInStock(),
                        item.getStockReorderLevel(),
                        DTO_Factory.create(item.getSupplier()));

        return itemDTO;
    }

    public static SupplierDTO create(Supplier supplier)
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

    public static ArrayList<ItemDTO> create(ArrayList<Item> items)
    {
        ArrayList<ItemDTO> list = new ArrayList<>(items.size());
        for (Item item : items)
        {
            list.add(DTO_Factory.create(item));
        }

        return list;
    }

    public static OrderDTO create(Order order, CustomerDTO custDTO)
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
                DTO_Factory.create(order.getOrderLines(), orderDTO));

        return orderDTO;
    }

    public static OrderLineDTO create(OrderLine line, OrderDTO orderDTO)
    {
        return new OrderLineDTO(
                DTO_Factory.create(line.getItem()),
                orderDTO,
                line.getOrderLineId(),
                line.getPrice(),
                line.getQuantity());
    }

    public static HashMap<Integer, OrderLineDTO> create(HashMap<Integer, OrderLine> lines, OrderDTO orderDTO)
    {
        if (lines == null)
        {
            return null;
        }

        HashMap<Integer, OrderLineDTO> orderLinesDTO = new HashMap<>(lines.size());
        for (OrderLine line : lines.values())
        {
            OrderLineDTO lineDTO = DTO_Factory.create(line, orderDTO);
            orderLinesDTO.put(lineDTO.getOrderLineId(), lineDTO);
        }
        return orderLinesDTO;
    }

    public static HashMap<Integer, OrderDTO> create(HashMap<Integer, Order> orders, CustomerDTO custDTO)
    {
        if (orders == null)
        {
            return null;
        }

        HashMap<Integer, OrderDTO> ordersDTO = new HashMap<>();
        for (Order order : orders.values())
        {
            OrderDTO orderDTO = DTO_Factory.create(order, custDTO);

            ordersDTO.put(orderDTO.getOrderId(), orderDTO);
        }
        return ordersDTO;
    }

    public static CustomerDTO create(Customer customer)
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
                DTO_Factory.create(customer.getOrders(), custDTO));
        return custDTO;
    }

}
