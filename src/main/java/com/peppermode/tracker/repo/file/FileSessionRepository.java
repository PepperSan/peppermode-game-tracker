package com.peppermode.tracker.repo.file;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.peppermode.tracker.domain.PlaySession;
import com.peppermode.tracker.repo.SessionRepository;
import com.peppermode.tracker.util.JsonUtil;

import java.io.IOException;
import java.lang.reflect.Type;
import java.nio.file.*;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

public class FileSessionRepository implements SessionRepository {

    private final Path file;
    private final Gson gson = JsonUtil.gson();
    private final Map<String, PlaySession> byId = new ConcurrentHashMap<>();

    private static final Type LIST_TYPE = new TypeToken<List<PlaySession>>(){}.getType();

    public FileSessionRepository() {
        this(Path.of("data", "sessions.json"));
    }

    public FileSessionRepository(Path file) {
        this.file = file;
        init();
    }

    private void init() {
        try {
            Files.createDirectories(file.getParent());
            if (Files.exists(file)) {
                String json = Files.readString(file);

                List<PlaySession> list = gson.fromJson(json, LIST_TYPE);
                if (list == null) list = new ArrayList<>();

                // загрузка в карту
                for (PlaySession s : list) {
                    byId.put(s.getId(), s);
                }
            } else {
                persist(); // создаём пустой файл
            }
        } catch (IOException e) {
            throw new RuntimeException("Failed to init sessions storage: " + file, e);
        }
    }


    private synchronized void persist() {
        try {
            String json = gson.toJson(new ArrayList<>(byId.values()), LIST_TYPE);
            Path tmp = Files.createTempFile(file.getParent(), "sessions", ".tmp");
            Files.writeString(tmp, json, StandardOpenOption.TRUNCATE_EXISTING);
            Files.move(tmp, file, StandardCopyOption.REPLACE_EXISTING, StandardCopyOption.ATOMIC_MOVE);
        } catch (IOException e) {
            throw new RuntimeException("Failed to write " + file, e);
        }
    }

    // -------- SessionRepository implementation --------



    @Override
    public Optional<PlaySession> findById(String id) {
        return Optional.ofNullable(byId.get(id));
    }

    @Override
    public List<PlaySession> findAll() {
        return new ArrayList<>(byId.values());
    }

    @Override
    public List<PlaySession> findByGameId(String gameId) {
        return byId.values().stream()
                .filter(s -> Objects.equals(s.getGameId(), gameId))
                .collect(Collectors.toList());
    }

    @Override
    public PlaySession save(PlaySession session) {
        Objects.requireNonNull(session, "session");
        Objects.requireNonNull(session.getId(), "session.id");
        byId.put(session.getId(), session);
        persist();
        return session; // <— вернуть
    }


    // опционально, если пригодится
    public void deleteById(String id) {
        if (byId.remove(id) != null) persist();
    }

    public void deleteAll() {
        byId.clear();
        persist();
    }
}
