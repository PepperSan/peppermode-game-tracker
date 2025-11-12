package com.peppermode.tracker;

import com.peppermode.tracker.domain.Game;
import com.peppermode.tracker.domain.PlaySession;
import com.peppermode.tracker.repo.GameRepository;
import com.peppermode.tracker.repo.SessionRepository;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

public class StatsService {
    private final GameRepository games;
    private final SessionRepository sessions;

    public StatsService(GameRepository games, SessionRepository sessions) {
        this.games = games;
        this.sessions = sessions;
    }

    public int totalMinutesForGame(String gameId) {
        return sessions.findByGameId(gameId).stream()
                .mapToInt(PlaySession::getMinutesPlayed)
                .sum();
    }

    public Map<String, Integer> totalMinutesByGame() {
        return sessions.findAll().stream()
                .collect(Collectors.groupingBy(PlaySession::getGameId,
                        Collectors.summingInt(PlaySession::getMinutesPlayed)));
    }

    public List<Game> topGamesByTime(int limit) {
        Map<String, Integer> totals = totalMinutesByGame();
        Map<String, Game> byId = games.findAll().stream()
                .collect(Collectors.toMap(Game::getId, Function.identity()));
        return totals.entrySet().stream()
                .sorted(Map.Entry.<String, Integer>comparingByValue().reversed())
                .limit(limit)
                .map(e -> byId.get(e.getKey()))
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

    public double averageSessionLengthMinutes() {
        return sessions.findAll().stream()
                .mapToInt(PlaySession::getMinutesPlayed)
                .average()
                .orElse(0.0);
    }

    public Map<String, Long> gamesCountByGenre() {
        return games.findAll().stream()
                .collect(Collectors.groupingBy(Game::getGenre, Collectors.counting()));
    }
}

