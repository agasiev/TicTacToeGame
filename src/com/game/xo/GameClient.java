package com.game.xo;

import java.net.*;
import java.io.*;
import java.util.Scanner;

public class GameClient extends Thread {
    BufferedWriter writer;
    BufferedReader reader;
    Socket socket;

    public GameClient(String host, int port) {
        try {
            socket = new Socket(host, port);
            writer = new BufferedWriter(new OutputStreamWriter(
                    socket.getOutputStream()));
            reader = new BufferedReader(new InputStreamReader(
                    socket.getInputStream()));
            System.out.println("Creating client connecton to " + host + ":" + port);
            socket.setSoTimeout(60000);
        }
        catch (Exception e) {
            System.out.println("[Error]: " + e.getMessage());
        }
    }

    private void putCommand(GameCommands command) {
        putCommand(command, "");
    }

    private void putCommand(GameCommands command, String params) {
        try {
            writer.write(command.getValue());
            writer.newLine();
            if (command.isNeedParams()) {
                writer.write(params);
                writer.newLine();
            }
            writer.flush();
        }
        catch (SocketTimeoutException e)
        {
            System.out.println("[Error]: " + e.getMessage());
        }
        catch(IOException e)
        {
            System.out.println("[Error]: " + e.getMessage());
        }
    }

    public void run()
    {
        if (socket == null) return;
        boolean doNotInput = false;
        boolean disconnected = false;
        try {
            putCommand(GameCommands.START_GAME);
            while(!disconnected)
            {
                try
                {
//                    Runtime.getRuntime().exec("clear");
                    String line = reader.readLine();
                    GameCommands command = GameCommands.getCommand(line);
                    String params = "";

                    if (command == null) {
                        putCommand(GameCommands.DISCONNECT);
                        disconnected = true;
                        break;
                    }

                    if (command.isNeedParams()) {
                        params = reader.readLine();
                    }

                    switch (command) {
                        case DISCONNECT: {
                            System.out.println("Server disconnected.");
                            disconnected = true;
                            break;
                        }
                        case PUBLIC_KEY: {
                            break;
                        }
                        case PRINT_MATRIX: {
                            for (int i = 0; i < 3; i++) {
                                System.out.format(" %c | %c | %c \n", params.charAt(i * 3), params.charAt(i * 3 + 1), params.charAt(i * 3 + 2));
                                if (i != 2)
                                    System.out.println("-----------");
                            }
                            if (doNotInput) disconnected = true;

                            break;
                        }
                        case CLIENT_WIN: {
                            System.out.println("YOU WIN!");
                            putCommand(GameCommands.DISCONNECT);
                            doNotInput = true;
                            break;
                        }
                        case SERVER_WIN: {
                            System.out.println("YOU LOSE!");
                            putCommand(GameCommands.DISCONNECT);
                            doNotInput = true;
                            break;
                        }
                        case DEAD_HEAT: {
                            System.out.println("DEAD HEAT!");
                            putCommand(GameCommands.DISCONNECT);
                            doNotInput = true;
                            disconnected = true;
                            break;
                        }
                        case MESSAGE: {
                            break;
                        }
                        case BAD_INPUT: {
                            System.out.println("Bad input.");
                            break;
                        }
                        case OK: {
                            break;
                        }
                    }
                    if (!doNotInput) {
                        Scanner input = new Scanner(System.in);

                        int x, y;

                        System.out.println("Make movement x y [1..3]:");

                        x = input.nextInt();
                        y = input.nextInt();

                        params = x + " " + y;

                        putCommand(GameCommands.PLAYER_MOVE, params);
                    }
                }
                catch (Exception e) {
                    System.out.println(e.getMessage());
                    e.printStackTrace();
                    break;
                }
            }

            writer.close();
            reader.close();
            socket.close();
        }
        catch (SocketTimeoutException e)
        {
            putCommand(GameCommands.DISCONNECT);
            System.out.println("[Timeout]: " + e.getMessage());
        }
        catch(IOException e)
        {
            System.out.println("[Error]: " + e.getMessage());
        }
    }
}