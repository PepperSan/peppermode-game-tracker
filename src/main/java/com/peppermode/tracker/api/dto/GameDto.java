package com.peppermode.tracker.api.dto;

import com.peppermode.tracker.domain.Game;
import jakarta.validation.constraints.NotBlank;

public record GameDto(
        String id,
        @NotBlank String title,
        String genre,
        String platform,
        Integer releaseYear
) {
    public static GameDto from(Game g) {
        return new GameDto(g.getId(), g.getTitle(), g.getGenre(), g.getPlatform(), g.getReleaseYear());
    }
    public Game toDomainWithId(String id) {
        return new Game(id, title, genre, platform, releaseYear);
    }
    public Game toDomainNew(String id) {
        return new Game(id, title, genre, platform, releaseYear);
    }
    public Game toDomain() {
        return new Game(id, title, genre, platform, releaseYear);
    }
}

