package com.peppermode.tracker.repo;

import com.peppermode.tracker.domain.Game;
import java.util.List;
import java.util.Optional;

public interface GameRepository {
    Game save(Game g);
    Optional<Game> findById(String id);
    List<Game> findAll();
    void deleteById(String id);
    void deleteAll();
}
