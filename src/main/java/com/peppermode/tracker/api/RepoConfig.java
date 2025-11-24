package com.peppermode.tracker.api;

import com.peppermode.tracker.repo.GameRepository;
import com.peppermode.tracker.repo.InMemoryGameRepository;
import com.peppermode.tracker.repo.InMemorySessionRepository;
import com.peppermode.tracker.repo.SessionRepository;
import com.peppermode.tracker.repo.file.FileGameRepository;
import com.peppermode.tracker.repo.file.FileSessionRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.nio.file.Path;
import java.nio.file.Paths;

@Configuration
public class RepoConfig {

    @Value("${pepper.storage.file:false}")
    private boolean useFileStorage;

    private static final Logger log = LoggerFactory.getLogger(RepoConfig.class);


    private static final Path GAMES_PATH =
            Paths.get(System.getProperty("user.dir"), "data", "games.json");

    private static final Path SESSIONS_PATH =
            Paths.get(System.getProperty("user.dir"), "data", "sessions.json");


    @Bean
    public GameRepository gameRepository() {
        log.info("gameRepository: useFileStorage = {}", useFileStorage);

        if (useFileStorage) {
            log.info("Using FileGameRepository");
            return new FileGameRepository(GAMES_PATH);
        } else {
            log.info("Using InMemoryGameRepository");
            return new InMemoryGameRepository();
        }
    }

    @Bean
    public SessionRepository sessionRepository() {
        log.info("sessionRepository: useFileStorage = {}", useFileStorage);

        if (useFileStorage) {
            log.info("Using FileSessionRepository");
            return new FileSessionRepository(SESSIONS_PATH);
        } else {
            log.info("Using InMemorySessionRepository");
            return new InMemorySessionRepository();
        }
    }


}
