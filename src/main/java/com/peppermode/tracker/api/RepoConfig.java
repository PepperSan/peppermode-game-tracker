package com.peppermode.tracker.api;

import com.peppermode.tracker.repo.GameRepository;
import com.peppermode.tracker.repo.SessionRepository;
import com.peppermode.tracker.repo.file.FileGameRepository;
import com.peppermode.tracker.repo.file.FileSessionRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.nio.file.Path;
import java.nio.file.Paths;

@Configuration
public class RepoConfig {

    private static final Path GAMES_PATH = Paths.get("data", "games.json");
    private static final Path SESSIONS_PATH = Paths.get("data", "sessions.json");

    @Bean
    public GameRepository gameRepository() {
        return new FileGameRepository(GAMES_PATH);
    }

    @Bean
    public SessionRepository sessionRepository() {
        return new FileSessionRepository(SESSIONS_PATH);
    }
}
