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
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
    }

    static void makeServer() {
        try {
            GameServerManager serverConnection = new GameServerManager(serverPort);
            serverConnection.start();
        }
        catch (Exception e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
//        makeServer();
        makeClient();
    }
}
