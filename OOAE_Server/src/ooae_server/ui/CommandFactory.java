/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ooae_server.ui;
/**
 *
 * @author f023507i
 */
public class CommandFactory
{

    public static Command createCommand(String command, String jsonInStr, String defaultOutStr)
    {
        
        switch (command)
        {
            case "addItem":
                return new AddItemCommand(jsonInStr, defaultOutStr);
            case "checkNeedForDatabaseInitialisation":
                return new CheckNeedForDatabaseInitialisationCommand(defaultOutStr);
            case "initialiseDatabase":
                return new InitialiseDatabaseCommand(defaultOutStr);
            case "shipOrder":
                return new ShipOrderCommand(jsonInStr, defaultOutStr);
            case "updateItemQuantityInStock":
                return new UpdateItemQuantityInStockCommand(jsonInStr, defaultOutStr);
            case "viewAllItems":
                return new ViewAllItemsCommand(jsonInStr, defaultOutStr);
            case "viewItem":
                return new ViewItemCommand(jsonInStr, defaultOutStr);
            case "viewItemsToReorder":
                return new ViewItemsToReorderCommand(defaultOutStr);
            case "cancelOrder":
                return new CancelOrderCommand(jsonInStr, defaultOutStr);
            case "login":
                return new LoginCommand(jsonInStr, defaultOutStr);
            case "placeOrder":
                return new PlaceOrderCommand(jsonInStr, defaultOutStr);
            case "viewMyOrders":
                return new ViewMyOrdersCommand(jsonInStr, defaultOutStr);

            default:
                return new UnknownCommand();
        }
    }

}
