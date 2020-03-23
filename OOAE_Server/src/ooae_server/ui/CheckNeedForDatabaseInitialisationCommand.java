/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ooae_server.ui;

import java.util.logging.Level;
import java.util.logging.Logger;
import ooae_server.AdminClientResponderCopy;
import ooae_server.database.AdminClientResponderGateway;
import ooae_server.database.CustomerGateway;
import ooae_server.database.ItemGateway;
import ooae_server.database.OrderGateway;
import ooae_server.database.SupplierGateway;

/**
 *
 * @author f023507i
 */
public class CheckNeedForDatabaseInitialisationCommand implements Command
{

    private String outStr;
    private final AdminClientResponderGateway acResponderGateway = new AdminClientResponderGateway();

    public CheckNeedForDatabaseInitialisationCommand(String outStr)
    {
        this.outStr = outStr;
    }

    @Override
    public String execute()
    {
        try
        {
            acResponderGateway.checkNeedForDatabaseInitialisation();
        } catch (Exception ex)
        {
            System.out.println(ex.getMessage());
            outStr = "ERROR: Unrecognised command";

        }
        return outStr;

    }

}
