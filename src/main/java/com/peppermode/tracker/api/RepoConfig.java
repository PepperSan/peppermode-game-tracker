package com.peppermode.tracker.api;

import com.peppermode.tracker.repo.*;
import com.peppermode.tracker.repo.file.FileGameRepository;
import com.peppermode.tracker.repo.file.FileSessionRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RepoConfig {
    @Bean
    public GameRepository gameRepository() {
        return new FileGameRepository(); // JSON-хранилище
    }
    @Bean
    public SessionRepository sessionRepository() {
        return new FileSessionRepository();
    }
}
