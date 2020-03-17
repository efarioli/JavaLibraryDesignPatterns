package ooae_server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author gdm1
 */
public class OOAE_Server
{

    private static final int PORT_NUMBER = 30000;

    public static void main(String[] args)
    {
        checkNeedForDatabaseInitialisation();

        try
        {
            ServerSocket serverSocket = new ServerSocket(PORT_NUMBER);
            System.out.println("Listening...");

            while (true)
            {
                Socket clientSocket = serverSocket.accept();
                BufferedReader clientIn = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                PrintWriter clientOut = new PrintWriter(clientSocket.getOutputStream(), true);

                String fromClient = clientIn.readLine();
//                System.out.println("Client says: " + fromClient);
                if (fromClient != null)
                {
                    String[] inputParts = fromClient.split("#");
                    String clientType = inputParts[0];

                    switch (clientType)
                    {
                        case "Admin":
                                Thread t1 = new Thread(new AdminClientResponder(clientSocket, clientOut, inputParts));
                                t1.start();
                                break;
                                
                        case "Customer":
                                Thread t2 = new Thread(new CustomerClientResponder(clientSocket, clientOut, inputParts));
                                t2.start();
                                break;
                                
                        default:
                            System.out.println("Unrecognised clientType");
                            break;
                    }
                }
            }
        }
        catch (IOException e)
        {
            Logger.getLogger(OOAE_Server.class.getName()).log(Level.SEVERE, null, e);
        }
    }

    private static void checkNeedForDatabaseInitialisation()
    {
        String[] inputParts =
        {
            "Admin", "checkNeedForDatabaseInitialisation"
        };
        PrintWriter clientOut = new PrintWriter(System.out);

        Thread t = new Thread(new AdminClientResponder(null, clientOut, inputParts));
        t.start();
        try
        {
            t.join();
        }
        catch (InterruptedException ex)
        {
            Logger.getLogger(OOAE_Server.class.getName()).log(Level.SEVERE, null, ex);
        }
        clientOut.println("Database checked");
        clientOut.flush();
    }
}
