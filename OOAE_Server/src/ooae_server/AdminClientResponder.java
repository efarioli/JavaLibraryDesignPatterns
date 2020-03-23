package ooae_server;

import java.io.PrintWriter;
import java.net.Socket;
import ooae_server.database.AdminClientResponderGateway;
import ooae_server.ui.CommandFactory;

/**
 *
 * @author gdm1
 */
public class AdminClientResponder implements Runnable
{

    private final PrintWriter clientOut;
    private final String command;
    private final String jsonInStr;
    private final Socket socket;
    private final AdminClientResponderGateway acResponderGateway = new AdminClientResponderGateway();

    public AdminClientResponder(Socket socket, PrintWriter clientOut, String[] inputParts)
    {
        this.socket = socket;
        this.clientOut = clientOut;
        command = inputParts[1];
        if (inputParts.length > 2)
        {
            jsonInStr = inputParts[2];
        } else
        {
            jsonInStr = null;
        }
    }

    @Override
    public void run()
    {
        String outStr = "OK";
        try
        {
            outStr = CommandFactory.createCommand(command, jsonInStr, outStr).execute();
            clientOut.println(outStr);
            clientOut.flush();
            if (socket != null)
            {
                socket.close();
            }
        } catch (Exception e)
        {
//            e.printStackTrace();
            clientOut.println(e.getMessage());
            clientOut.flush();
        }
    }

    private void checkNeedForDatabaseInitialisation() throws Exception
    {
        acResponderGateway.checkNeedForDatabaseInitialisation();

    }

    private void initialiseDatabase() throws Exception
    {
        acResponderGateway.initialiseDatabase();
    }
}
