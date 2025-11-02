package com.peppermode.tracker.repo;

import com.peppermode.tracker.domain.PlaySession;
import java.util.List;
import java.util.Optional;

public interface SessionRepository {
    PlaySession save(PlaySession s);
    Optional<PlaySession> findById(String id);
    List<PlaySession> findByGameId(String gameId);
    List<PlaySession> findAll();
}

