package com.peppermode.tracker.domain;

import java.time.LocalDateTime;
import java.util.UUID;

public class PlaySession {
    private String id = UUID.randomUUID().toString();
    private String gameId;
    private LocalDateTime start;
    private int minutesPlayed;
    private String characterName;
    private String notes;

    public PlaySession() { }

    public PlaySession(String gameId, LocalDateTime start, int minutesPlayed, String characterName, String notes) {
        this.gameId = gameId;
        this.start = start;
        this.minutesPlayed = minutesPlayed;
        this.characterName = characterName;
        this.notes = notes;
    }

    public String getId() { return id; }
    public String getGameId() { return gameId; }
    public LocalDateTime getStart() { return start; }
    public int getMinutesPlayed() { return minutesPlayed; }
    public String getCharacterName() { return characterName; }
    public String getNotes() { return notes; }
}

