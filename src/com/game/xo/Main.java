package com.game.xo;

import java.util.Scanner;

public class Main {
    static final int serverPort = 11098;
    static final String serverHost = "localhost";

    static void makeClient() {
        try {
            GameClient clientConnection = new GameClient(serverHost, serverPort);
            clientConnection.start();
        }
        catch (Exception e) {
            System.out.println("[Error]: " + e.getMessage());
        }
    }

    static void makeServer() {
        try {
            GameServerManager serverConnection = new GameServerManager(serverPort);
            serverConnection.start();
        }
        catch (Exception e) {
            System.out.println("[Error]: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        try {
            Scanner input = new Scanner(System.in);

            System.out.println("Please specify task:\n1. Start server.\n2. Start client.");

            int value = input.nextInt();
            if (value == 1) makeServer();
            else if (value == 2) makeClient();
            else System.out.println("Wrong input!");
        }
        catch (Exception e) {
            System.out.println("[Error]: " + e.getMessage());
        }
    }
}
