package com.game.xo;

public enum GameCommands {
    START_GAME("start_game"),
    PRINT_MATRIX("print_matrix", true),
    PLAYER_MOVE("player_move", true),
    CLIENT_WIN("client_win"),
    SERVER_WIN("server_win"),
    DEAD_HEAT("dead_heat"),
    DISCONNECT("disconnect"),
    BAD_INPUT("bad_input");

    private String command;
    private boolean needParams = false;

    GameCommands(String command) {
        this.command = command;
        this.needParams = false;
    }

    GameCommands(String command, boolean params) {
        this.command = command;
        this.needParams = params;
    }

    boolean isNeedParams() {
        return needParams;
    }

    String getValue() {
        return command;
    }

    public static GameCommands getCommand(String value) {
        for (GameCommands item : values()) {
            if (item.command.equals(value)) return item;
        }
        return null;
    }
}