/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ooae_server.ui;

import ooae_server.database.AdminClientResponderGateway;

/**
 *
 * @author f023507i
 */
public class InitialiseDatabaseCommand implements Command
{

    private final AdminClientResponderGateway acResponderGateway = new AdminClientResponderGateway();
    private String outStr;

    public InitialiseDatabaseCommand(String outStr)
    {
        this.outStr = outStr;
    }

    @Override
    public String execute()
    {
        try
        {
            acResponderGateway.initialiseDatabase();
        } catch (Exception ex)
        {
            System.out.println(ex.getMessage());
            outStr = "ERROR: Unrecognised command";
        }
        return outStr;
    }
}
