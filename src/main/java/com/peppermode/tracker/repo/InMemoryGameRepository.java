package com.peppermode.tracker.repo;

import com.peppermode.tracker.domain.Game;
import java.util.*;

public class InMemoryGameRepository implements GameRepository {
    private final Map<String, Game> store = new LinkedHashMap<>();

    @Override
    public Game save(Game g) {
        store.put(g.getId(), g);
        return g;
    }

    @Override
    public Optional<Game> findById(String id) {
        return Optional.ofNullable(store.get(id));
    }

    @Override
    public List<Game> findAll() {
        return new ArrayList<>(store.values());
    }
    @Override
    public void deleteById(String id) {
        store.remove(id);
    }

    @Override
    public void deleteAll() {
        store.clear();
    }

}

