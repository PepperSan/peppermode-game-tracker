package com.peppermode.tracker.service;

import com.peppermode.tracker.domain.Game;
import com.peppermode.tracker.repo.GameRepository;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;

@Service
public class GameService {

    private final GameRepository gameRepository;

    public GameService(GameRepository gameRepository) {
        this.gameRepository = gameRepository;
    }

    public List<Game> getAllGames() {
        return gameRepository.findAll(); // БЫЛО findALL() — это опечатка
    }

    public List<Game> findGames(String genre,
                                String platform,
                                Integer yearFrom,
                                Integer yearTo,
                                String sort) {

        var stream = gameRepository.findAll().stream()
                .filter(g -> genre == null || genre.isBlank()
                        || (g.getGenre() != null && g.getGenre().equalsIgnoreCase(genre)))
                .filter(g -> platform == null || platform.isBlank()
                        || (g.getPlatform() != null && g.getPlatform().equalsIgnoreCase(platform)))
                .filter(g -> yearFrom == null || g.getReleaseYear() >= yearFrom)
                .filter(g -> yearTo == null || g.getReleaseYear() <= yearTo);

        // ---------- сортировка ----------
        if (sort != null && !sort.isBlank()) {
            boolean desc = sort.startsWith("-");
            String key = desc ? sort.substring(1) : sort;

            Comparator<Game> comparator = switch (key) {
                case "title" -> Comparator.comparing(
                        Game::getTitle,
                        String.CASE_INSENSITIVE_ORDER
                );
                case "year" -> Comparator.comparingInt(Game::getReleaseYear);
                case "platform" -> Comparator.comparing(
                        Game::getPlatform,
                        String.CASE_INSENSITIVE_ORDER
                );
                default -> null;
            };

            if (comparator != null) {
                if (desc) {
                    comparator = comparator.reversed();
                }
                stream = stream.sorted(comparator);
            }
        }

        return stream.toList();
    }
}
