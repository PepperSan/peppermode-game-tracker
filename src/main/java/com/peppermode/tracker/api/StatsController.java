package com.peppermode.tracker.api;

import com.peppermode.tracker.api.dto.AverageSessionDto;
import com.peppermode.tracker.api.dto.GameStatsDto;
import com.peppermode.tracker.api.dto.GenreStatsDto;
import com.peppermode.tracker.domain.Game;
import com.peppermode.tracker.repo.GameRepository;
import com.peppermode.tracker.repo.SessionRepository;
import com.peppermode.tracker.StatsService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/stats")
public class StatsController {

    private final SessionRepository sessions;
    private final GameRepository games;
    private final StatsService statsService;

    public StatsController(SessionRepository sessions, GameRepository games) {
        this.sessions = sessions;
        this.games = games;
        this.statsService = new StatsService(games, sessions); // именно (games, sessions)
    }

    @GetMapping("/games")
    public List<GameStatsDto> gameStats() {

        var totals = statsService.totalMinutesByGame(); // ✅ без аргумента

        Map<String, Game> gameById = games.findAll().stream()
                .collect(Collectors.toMap(Game::getId, Function.identity()));

        return totals.entrySet().stream()
                .map(e -> {
                    var game = gameById.get(e.getKey());
                    String title = game != null ? game.getTitle() : "Unknown";
                    return new GameStatsDto(e.getKey(), title, e.getValue());
                })
                .sorted(Comparator.comparingInt(GameStatsDto::getTotalMinutes).reversed())
                .toList();
    }

    @GetMapping("/games/top")
    public List<GameStatsDto> topGames(
            @RequestParam(defaultValue = "3") int limit
    ) {
        return gameStats().stream()
                .limit(limit)
                .toList();
    }

    @GetMapping("/sessions/average")
    public AverageSessionDto averageSession() {
        double avg = statsService.averageSessionLengthMinutes();
        return new AverageSessionDto(avg);
    }

    @GetMapping("/genres")
    public List<GenreStatsDto> countByGenre() {
        return statsService.gamesCountByGenre().entrySet().stream()
                .map(e -> new GenreStatsDto(e.getKey(), e.getValue()))
                .toList();
    }


}

