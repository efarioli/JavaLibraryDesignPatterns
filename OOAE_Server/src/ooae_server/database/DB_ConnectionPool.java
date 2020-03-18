/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ooae_server.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 *
 * @author f023507i
 */
public class DB_ConnectionPool
{
    private static final DB_ConnectionPool INSTANCE = new DB_ConnectionPool();

    private final String dbaseURL = "jdbc:derby://localhost:1527/OOAEassignment;user=ooae;password=ooae";
    private final ArrayList<Connection> available = new ArrayList<Connection>();
    private final ArrayList<Connection> busy = new ArrayList<Connection>();
    private int sizeOfPool = 20;//Can be any number, according to the optimal number of simultaneous connections, specified in the database specifications

    public static final DB_ConnectionPool getInstance()
    {
        return INSTANCE;
    }

    public DB_ConnectionPool()
    {
        for (int i = 0; i < sizeOfPool; i++)
        {
            try
            {
                DriverManager.registerDriver(
                        new org.apache.derby.jdbc.ClientDriver());
                available.add(DriverManager.getConnection(dbaseURL, "ooae", "ooae"));
            }
            catch (SQLException e)
            {
                e.printStackTrace();
            }
        }
    }

    //if no more connection avaible return null, nullobject or ExcextionNoConnectionAvailable
    Connection acquireConnection()
    {
        if (!available.isEmpty())
        {
            Connection c = available.get(0);
            available.remove(c);
            busy.add(c);
            return c;
        }
        else
        {
            return null;
        }

    }

    void releaseConnection(Connection conn)
    {
        available.add(conn);
        busy.remove(conn);

    }

}
