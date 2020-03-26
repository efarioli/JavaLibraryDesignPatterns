/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ooae_server.entity.builder;

import ooae_server.entity.Item;
import ooae_server.entity.Supplier;

/**
 *
 * @author f023507i
 */
public class ItemBuilder
{

    private String description;
    private int itemId;
    private String name;
    private double price;
    private int quantityInStock;
    private int stockReorderLevel;
    private Supplier supplier;
    private Item item;

    public ItemBuilder()
    {
        description = null;
        itemId = 0;
        name = null;
        quantityInStock = 0;
        stockReorderLevel = 0;
        supplier = null;
        item = new Item(description, itemId, name, price, quantityInStock, stockReorderLevel, supplier);
    }

    public ItemBuilder withDescription(String description)
    {
        this.description = description;
        return this;
    }

    public ItemBuilder withItemId(int itemId)
    {
        this.itemId = itemId;
        return this;
    }
     public ItemBuilder withName(String name)
    {
        this.name = name;
        return this;
    }

    public ItemBuilder withPrice(double price)
    {
        this.price = price;
        return this;
    }

    public ItemBuilder withQuantityInStock(int quantityInStock)
    {
        this.quantityInStock = quantityInStock;
        return this;
    }

    public ItemBuilder withSupplier(Supplier supplier)
    {
        this.supplier = supplier;
        return this;
    }

    public Item build()
    {
        Item returnedItem = new Item(description, itemId, name,
                price, quantityInStock, stockReorderLevel, supplier);
        return returnedItem;

    }
}
