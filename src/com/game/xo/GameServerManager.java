package com.game.xo;

import java.net.*;
import java.io.*;

public class GameServerManager extends Thread {
    ServerSocket serverSocket;

    public GameServerManager(int localPort) {
        try {
            serverSocket = new ServerSocket(localPort);
            System.out.println("Creating server connecton on port " + serverSocket.getLocalPort());
        }
        catch (Exception e) {
            System.out.println("[Error]: " + e.getMessage());
        }
    }

    public void run()
    {
        while(true)
        {
            try
            {
                System.out.println("Waiting for client on port " +
                        serverSocket.getLocalPort() + "...");
                (new GameServerInstance(serverSocket.accept())).start();
            }
            catch (SocketTimeoutException e)
            {
                System.out.println("[Timeout]: " + e.getMessage());
                break;
            }
            catch(IOException e)
            {
                System.out.println("[Error]: " + e.getMessage());
                break;
            }
        }

        System.out.println("Stopping server on port " + serverSocket.getLocalPort());
    }
}
