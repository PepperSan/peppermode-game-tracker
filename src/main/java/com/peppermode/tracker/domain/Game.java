package com.peppermode.tracker.domain;

import java.util.Objects;
import java.util.UUID;

public class Game {
    private String id = UUID.randomUUID().toString();
    private String title;
    private String genre;
    private String platform;
    private int releaseYear;


    public Game() { }

    public Game(String id, String title, String genre, String platform, Integer releaseYear) {
        this.id = id;
        this.title = title;
        this.genre = genre;
        this.platform = platform;
        this.releaseYear = releaseYear;
    }


    public Game(String title, String genre, String platform, int releaseYear) {
        this.title = title;
        this.genre = genre;
        this.platform = platform;
        this.releaseYear = releaseYear;
    }

    public String getId() { return id; }
    public String getTitle() { return title; }
    public String getGenre() { return genre; }
    public String getPlatform() { return platform; }
    public int getReleaseYear() { return releaseYear; }

    public void setTitle(String title) { this.title = title; }
    public void setGenre(String genre) { this.genre = genre; }
    public void setPlatform(String platform) { this.platform = platform; }
    public void setReleaseYear(int releaseYear) { this.releaseYear = releaseYear; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Game)) return false;
        Game game = (Game) o;
        return Objects.equals(id, game.id);
    }

    @Override
    public int hashCode() { return Objects.hash(id); }

    @Override
    public String toString() {
        return title + " (" + platform + ", " + releaseYear + ")";
    }
}
