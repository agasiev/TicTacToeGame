package com.game.xo;

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
//        makeServer();
        makeClient();
    }
}
