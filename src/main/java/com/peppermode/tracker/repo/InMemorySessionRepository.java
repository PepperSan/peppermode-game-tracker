package com.peppermode.tracker.repo;

import com.peppermode.tracker.domain.PlaySession;
import java.util.*;
import java.util.stream.Collectors;

public class InMemorySessionRepository implements SessionRepository {
    private final Map<String, PlaySession> store = new LinkedHashMap<>();

    @Override
    public PlaySession save(PlaySession s) {
        store.put(s.getId(), s);
        return s;
    }

    @Override
    public Optional<PlaySession> findById(String id) {
        return Optional.ofNullable(store.get(id));
    }

    @Override
    public List<PlaySession> findByGameId(String gameId) {
        return store.values().stream()
                .filter(s -> s.getGameId().equals(gameId))
                .collect(Collectors.toList());
    }

    @Override
    public List<PlaySession> findAll() {
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
