package com.peppermode.tracker.api.dto;

import com.peppermode.tracker.domain.PlaySession;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

import java.time.LocalDateTime;

public record SessionDto(
        String id,
        @NotBlank String gameId,
        LocalDateTime start,
        @Min(1) int minutesPlayed,
        String characterName,
        String notes
) {
    public static SessionDto from(PlaySession s) {
        return new SessionDto(s.getId(), s.getGameId(), s.getStart(), s.getMinutesPlayed(), s.getCharacterName(), s.getNotes());
    }
    public PlaySession toDomainNew() {
        return new PlaySession(gameId, start != null ? start : LocalDateTime.now(), minutesPlayed, characterName, notes);
    }
}

