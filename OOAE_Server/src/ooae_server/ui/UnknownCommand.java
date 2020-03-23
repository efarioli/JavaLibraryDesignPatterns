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
public class UnknownCommand implements Command
{

    public UnknownCommand()
    {
    }

    @Override
    public String execute()
    {
        return "ERROR: Unrecognised command";

    }

}
