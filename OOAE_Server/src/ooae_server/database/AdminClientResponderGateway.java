/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ooae_server.database;

/**
 *
 * @author f023507i
 */
public class AdminClientResponderGateway
{
     public void checkNeedForDatabaseInitialisation() throws Exception
    {
        CustomerGateway customerTable = new CustomerGateway();
        ItemGateway itemTable = new ItemGateway();
        OrderGateway orderTable = new OrderGateway();
        SupplierGateway supplierTable = new SupplierGateway();

        if (!customerTable.exists()
                || !itemTable.exists()
                || !orderTable.exists()
                || !supplierTable.exists())
        {
            initialiseDatabase();
        }
    }

    public void initialiseDatabase() throws Exception
    {
        // acResponderGateway.initialiseDatabase();
        CustomerGateway customerTable = new CustomerGateway();
        ItemGateway itemTable = new ItemGateway();
        OrderGateway orderTable = new OrderGateway();
        SupplierGateway supplierTable = new SupplierGateway();
        String errorMsgs = "";

        try
        {
            orderTable.dropTable();
            itemTable.dropTable();
            supplierTable.dropTable();
            customerTable.dropTable();

            customerTable.initialiseTable();
            supplierTable.initialiseTable();
            itemTable.initialiseTable();
            orderTable.initialiseTable();
        } catch (Exception e)
        {
            errorMsgs += e.getMessage() + "\n";
        }

        if (!errorMsgs.isEmpty())
        {
            throw new Exception(errorMsgs);
        }

    }
    
}
