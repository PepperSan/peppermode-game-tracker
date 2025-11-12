package com.peppermode.tracker;

import com.peppermode.tracker.domain.Game;
import com.peppermode.tracker.domain.PlaySession;
import com.peppermode.tracker.repo.InMemoryGameRepository;
import com.peppermode.tracker.repo.InMemorySessionRepository;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class StatsServiceTest {

    @Test
    void testTotalMinutesForGame() {
        var gameRepo = new InMemoryGameRepository();
        var sessionRepo = new InMemorySessionRepository();

        Game g1 = new Game("g1", "Ghost of Tsushima", "Action", "PS5", 2020);
        Game g2 = new Game("g2", "Cyberpunk 2077", "RPG", "PC", 2020);
        gameRepo.save(g1);
        gameRepo.save(g2);

        sessionRepo.save(new PlaySession("g1", LocalDateTime.now(), 120, "Jin", ""));
        sessionRepo.save(new PlaySession("g1", LocalDateTime.now(), 45, "Jin", ""));
        sessionRepo.save(new PlaySession("g2", LocalDateTime.now(), 90, "V", ""));

        StatsService stats = new StatsService(gameRepo, sessionRepo);

        assertEquals(165, stats.totalMinutesForGame("g1"));
        assertEquals(90, stats.totalMinutesForGame("g2"));
    }

    @Test
    void testTopGamesByTime() {
        var gameRepo = new InMemoryGameRepository();
        var sessionRepo = new InMemorySessionRepository();

        Game g1 = new Game("g1", "Witcher 3", "RPG", "PC", 2015);
        Game g2 = new Game("g2", "Death Stranding", "Adventure", "PS5", 2019);
        Game g3 = new Game("g3", "Ghost of Tsushima", "Action", "PS5", 2020);

        gameRepo.save(g1);
        gameRepo.save(g2);
        gameRepo.save(g3);

        sessionRepo.save(new PlaySession("g1", LocalDateTime.now(), 300, "Geralt", ""));
        sessionRepo.save(new PlaySession("g2", LocalDateTime.now(), 200, "Sam", ""));
        sessionRepo.save(new PlaySession("g3", LocalDateTime.now(), 100, "Jin", ""));

        StatsService stats = new StatsService(gameRepo, sessionRepo);
        List<Game> top = stats.topGamesByTime(3);

        assertEquals("g1", top.get(0).getId());
        assertEquals("g2", top.get(1).getId());
        assertEquals("g3", top.get(2).getId());
    }

    @Test
    void testTotalMinutesByGameMap() {
        var gameRepo = new InMemoryGameRepository();
        var sessionRepo = new InMemorySessionRepository();

        Game g1 = new Game("g1", "AC Shadows", "Action", "PS5", 2025);
        gameRepo.save(g1);

        sessionRepo.save(new PlaySession("g1", LocalDateTime.now(), 60, "Yasuke", ""));
        sessionRepo.save(new PlaySession("g1", LocalDateTime.now(), 120, "Naoe", ""));

        StatsService stats = new StatsService(gameRepo, sessionRepo);

        var totals = stats.totalMinutesByGame();
        assertEquals(180, totals.get("g1"));
    }
}
