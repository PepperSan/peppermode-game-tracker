package com.peppermode.tracker.cli;

public enum Command {
    ADD_GAME("1"),
    ADD_SESSION("2"),
    SHOW_TOP_GAMES("3"),
    SHOW_AVG_SESSION("4"),
    SHOW_COUNTS_BY_GENRE("5"),
    LIST_GAMES("6"),
    LIST_SESSIONS_BY_GAME("7"),
    EXPORT_CSV("8"),
    EXIT("0"),
    UNKNOWN("");

    private final String code;
    Command(String code) { this.code = code; }

    public static Command from(String s) {
        for (Command c : values()) if (c.code.equals(s)) return c;
        return UNKNOWN;
    }
}

