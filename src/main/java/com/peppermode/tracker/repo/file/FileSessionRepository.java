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
    private final Path path = Path.of("data", "sessions.json");

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
    public List<PlaySession> findAll() {
        return JsonUtil.readList(path, PlaySession[].class);
    }

    @Override
    public Optional<PlaySession> findById(String id) {
        return findAll().stream()
                .filter(s -> s.getId().equals(id))
                .findFirst();
    }


    @Override
    public List<PlaySession> findByGameId(String gameId) {
        return JsonUtil.readList(path, PlaySession[].class)
                .stream()
                .filter(s -> Objects.equals(s.getGameId(), gameId))
                .toList();
    }



    @Override
    public PlaySession save(PlaySession session) {
        Objects.requireNonNull(session, "session");
        Objects.requireNonNull(session.getId(), "session.id");

        List<PlaySession> list = JsonUtil.readList(path, PlaySession[].class);

        list.removeIf(s -> s.getId().equals(session.getId()));
        list.add(session);

        JsonUtil.writeList(path, list);
        return session;
    }




    @Override
    public void deleteById(String id) {
        List<PlaySession> sessions = JsonUtil.readList(path, PlaySession[].class);
        sessions.removeIf(s -> s.getId().equals(id));
        JsonUtil.writeList(path, sessions);
    }


    @Override
    public void deleteAll() {
        JsonUtil.writeList(path, new ArrayList<>());
    }

}
