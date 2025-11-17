package com.peppermode.tracker.api.dto;

public class GameStatsDto {
    private final String gameId;
    private final String title;
    private final int totalMinutes;

    public GameStatsDto(String gameId, String title, int totalMinutes) {
        this.gameId = gameId;
        this.title = title;
        this.totalMinutes = totalMinutes;
    }

    public String getGameId() {
        return gameId;
    }

    public String getTitle() {
        return title;
    }

    public int getTotalMinutes() {
        return totalMinutes;
    }
}

