package com.peppermode.tracker.service;

import com.peppermode.tracker.api.dto.GameDto;
import com.peppermode.tracker.domain.Game;
import com.peppermode.tracker.repo.GameRepository;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Stream;

@Service
public class GameService {

    private final GameRepository gameRepository;

    private static final String GAME_NOT_FOUND = "Game not found: ";

    public GameService(GameRepository gameRepository) {
        this.gameRepository = gameRepository;
    }

    public List<Game> findGames(String genre,
                                String platform,
                                Integer yearFrom,
                                Integer yearTo,
                                String sort) {

        var stream = gameRepository.findAll().stream();
        stream = applyFilters(stream, genre, platform, yearFrom, yearTo);
        stream = applySorting(stream, sort);

        return stream.toList();
    }

    private Stream<Game> applyFilters(Stream<Game> stream,
                                      String genre,
                                      String platform,
                                      Integer yearFrom,
                                      Integer yearTo) {

        if (genre != null && !genre.isBlank()) {
            stream = stream.filter(g -> genre.equalsIgnoreCase(g.getGenre()));
        }

        if (platform != null && !platform.isBlank()) {
            stream = stream.filter(g -> platform.equalsIgnoreCase(g.getPlatform()));
        }

        // фильтрация по годам — в одном месте
        if (yearFrom != null || yearTo != null) {
            stream = stream.filter(g -> yearInRange(g, yearFrom, yearTo));
        }

        return stream;
    }

    private boolean yearInRange(Game g, Integer from, Integer to) {
        Integer y = g.getReleaseYear();
        if (y == null) {
            return false;
        }
        if (from != null && y < from) {
            return false;
        }
        if (to != null && y > to) {
            return false;
        }
        return true;
    }


    private Stream<Game> applySorting(Stream<Game> stream, String sort) {
        if (sort == null || sort.isBlank()) {
            return stream;
        }

        boolean desc = sort.startsWith("-");
        String key = desc ? sort.substring(1) : sort;

        Comparator<Game> comparator = switch (key) {
            case "title" -> Comparator.comparing(
                    Game::getTitle,
                    String.CASE_INSENSITIVE_ORDER
            );
            case "year" -> Comparator.comparingInt(this::releaseYearOrDefault);
            case "platform" -> Comparator.comparing(
                    Game::getPlatform,
                    String.CASE_INSENSITIVE_ORDER
            );
            default -> null;
        };

        if (comparator == null) {
            return stream;
        }

        if (desc) {
            comparator = comparator.reversed();
        }

        return stream.sorted(comparator);
    }

    private int releaseYearOrDefault(Game g) {
        Integer year = g.getReleaseYear();
        return year != null ? year : 0;
    }


    public GameDto update(String id, GameDto dto) {
        var existing = gameRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException(GAME_NOT_FOUND + id));
        Game updated = dto.toDomainWithId(existing.getId());

        Game saved = gameRepository.save(updated);

        return GameDto.from(saved);

    }

}
