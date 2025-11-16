package com.peppermode.tracker.service;

import com.peppermode.tracker.domain.Game;
import com.peppermode.tracker.repo.GameRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GameService {

    private final GameRepository gameRepository;

    public GameService(GameRepository gameRepository) {
        this.gameRepository = gameRepository;
    }

    public List<Game> getAllGames() {
        return gameRepository.findAll();
    }

    public List<Game> findGames(String genre, String platform,
                                Integer yearFrom, Integer yearTo) {
        var games = gameRepository.findAll();

        return games.stream()
                .filter(g -> genre == null || genre.isBlank()
                        || (g.getGenre() != null && g.getGenre().equalsIgnoreCase(genre)))
                .filter(g -> platform == null || platform.isBlank()
                        || (g.getPlatform() != null && g.getPlatform().equalsIgnoreCase(platform)))
                .filter(g -> yearFrom == null || g.getReleaseYear() >= yearFrom)
                .filter(g -> yearTo == null || g.getReleaseYear() <= yearTo)
                .toList();
    }

}

