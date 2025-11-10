package com.peppermode.tracker.repo.file;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.peppermode.tracker.domain.Game;
import com.peppermode.tracker.repo.GameRepository;
import com.peppermode.tracker.util.JsonUtil;

import java.io.IOException;
import java.lang.reflect.Type;
import java.nio.file.*;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class FileGameRepository implements GameRepository {

    private final Path file;
    private final Gson gson = JsonUtil.gson();
    private final Map<String, Game> byId = new ConcurrentHashMap<>();

    private static final Type LIST_TYPE = new TypeToken<List<Game>>(){}.getType();

    public FileGameRepository() {
        this(Path.of("data", "games.json"));
    }

    public FileGameRepository(Path file) {
        this.file = file;
        init();
    }

    private void init() {
        try {
            Files.createDirectories(file.getParent());
            if (Files.exists(file)) {
                String json = Files.readString(file);

                List<Game> list = gson.fromJson(json, LIST_TYPE);
                if (list == null) list = new ArrayList<>();

                for (Game g : list) {
                    byId.put(g.getId(), g);
                }
            } else {
                persist(); // создаём пустой файл
            }
        } catch (IOException e) {
            throw new RuntimeException("Failed to init games storage: " + file, e);
        }
    }


    private synchronized void persist() {
        try {
            var json = gson.toJson(new ArrayList<>(byId.values()), LIST_TYPE);
            // атомарная запись
            Path tmp = Files.createTempFile(file.getParent(), "games", ".tmp");
            Files.writeString(tmp, json, StandardOpenOption.TRUNCATE_EXISTING);
            Files.move(tmp, file, StandardCopyOption.REPLACE_EXISTING, StandardCopyOption.ATOMIC_MOVE);
        } catch (IOException e) {
            throw new RuntimeException("Failed to write " + file, e);
        }
    }

    // --- interface impl ---

    @Override
    public Optional<Game> findById(String id) {
        return Optional.ofNullable(byId.get(id));
    }

    @Override
    public List<Game> findAll() {
        return new ArrayList<>(byId.values());
    }

    @Override
    public Game save(Game game) {
        Objects.requireNonNull(game, "game");
        Objects.requireNonNull(game.getId(), "game.id");
        byId.put(game.getId(), game);
        persist();
        return game; // <— ВАЖНО: вернуть сохранённый объект
    }

    // если в интерфейсе есть delete/update — добавь по аналогии:
    public void deleteById(String id) {
        if (byId.remove(id) != null) persist();
    }

    public void deleteAll() {
        byId.clear();
        persist();
    }
}

