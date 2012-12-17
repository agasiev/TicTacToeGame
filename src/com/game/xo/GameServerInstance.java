package com.game.xo;

import java.net.*;
import java.io.*;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class GameServerInstance extends Thread {
    static int connectionsCount = 0;
    BufferedWriter writer;
    BufferedReader reader;
    Socket socket;
    GameModel model;

    public GameServerInstance(Socket server) {
        try {
            socket = server;
            connectionsCount++;
            System.out.println("Server instance created (" + connectionsCount + ")");

            writer = new BufferedWriter(new OutputStreamWriter(
                    socket.getOutputStream()));
            reader = new BufferedReader(new InputStreamReader(
                    socket.getInputStream()));
        }
        catch (Exception e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
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
            System.out.println(e.getMessage());
        }
        catch(IOException e)
        {
            System.out.println(e.getMessage());
        }
    }

    public void run()
    {
        model = new GameModel();
        boolean disconnected = false;
        while(!disconnected)
        {
            try
            {
                String line = reader.readLine();
                GameCommands command = GameCommands.getCommand(line);
                String params = "";

                if (command == null) {
                    putCommand(GameCommands.DISCONNECT);
                    break;
                }

                if (command.isNeedParams()) {
                    params = reader.readLine();
                }

                System.out.println("[Command]: "+command.getValue());

                switch (command) {
                    case DISCONNECT: {
                        System.out.println("Client disconnected.");
                        disconnected = true;
                        break;
                    }
                    case START_GAME: {
                        model.fillArray();
                        putCommand(GameCommands.PRINT_MATRIX, model.oneLineMatrix());
                        break;
                    }
                    case PUBLIC_KEY: {
                        break;
                    }
                    case PRINT_MATRIX: {
                        break;
                    }
                    case PLAYER_MOVE: {
                        Pattern p = Pattern.compile("[0-9]+");
                        Matcher m = p.matcher(params);

                        ArrayList<Integer> values = new ArrayList<Integer>();
                        while (m.find()) {
                            Integer a = Integer.parseInt(m.group());
                            values.add(a.intValue());
                        }
                        if (values.size() != 2)
                            putCommand(GameCommands.BAD_INPUT);

                        int x = values.get(0)-1;
                        int y = values.get(1)-1;

                        if (x < 0 || x >= 3 || y < 0 || y >= 3) {
                            putCommand(GameCommands.BAD_INPUT);
                            break;
                        }

                        boolean result = model.makePlayerMove(x, y);
                        GameModel.Winner winner = model.checkWinnings();

                        if (winner == GameModel.Winner.WIN_NONE) {
                            if (model.checkDeadHeat()) {
                                putCommand(GameCommands.DEAD_HEAT);
                                break;
                            }
                            else if (!result) {
                                putCommand(GameCommands.BAD_INPUT);
                                break;
                            }
                        }
                        else if (winner == GameModel.Winner.WIN_X) {
                            putCommand(GameCommands.CLIENT_WIN);
                        }
                        else {
                            putCommand(GameCommands.SERVER_WIN);
                        }

                        putCommand(GameCommands.PRINT_MATRIX, model.oneLineMatrix());

                        break;
                    }
                    case CLIENT_WIN: {

                        break;
                    }
                    case SERVER_WIN: {
                        break;
                    }
                    case MESSAGE: {
                        break;
                    }
                }
            }
            catch (SocketTimeoutException e)
            {
                System.out.println(e.getMessage());
            }
            catch(IOException e)
            {
                System.out.println(e.getMessage());
                e.printStackTrace();
                break;
            }
        }

        try {
            connectionsCount--;
            socket.close();
//            reader.close();
//            writer.close();
            System.out.println("Server thread stopped.");
        }
        catch (Exception e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
    }
}
