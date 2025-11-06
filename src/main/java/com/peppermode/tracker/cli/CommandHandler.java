package com.peppermode.tracker.cli;

import com.peppermode.tracker.domain.Game;
import com.peppermode.tracker.domain.PlaySession;
import com.peppermode.tracker.repo.GameRepository;
import com.peppermode.tracker.repo.SessionRepository;
import com.peppermode.tracker.service.StatsService;

import java.util.*;
import java.util.stream.Collectors;

public class CommandHandler {
    private final GameRepository gameRepo;
    private final SessionRepository sessionRepo;
    private final StatsService stats;
    private final Scanner sc;

    public CommandHandler(GameRepository gameRepo, SessionRepository sessionRepo, StatsService stats, Scanner sc) {
        this.gameRepo = gameRepo;
        this.sessionRepo = sessionRepo;
        this.stats = stats;
        this.sc = sc;
    }

    public void handle(Command cmd) {
        switch (cmd) {
            case ADD_GAME -> addGame();
            case ADD_SESSION -> addSession();
            case SHOW_TOP_GAMES -> showTopGames();
            case SHOW_AVG_SESSION -> showAverageSession();
            case SHOW_COUNTS_BY_GENRE -> showCountsByGenre();
            case LIST_GAMES -> listAllGames();
            case LIST_SESSIONS_BY_GAME -> listSessionsByGame();
            case EXPORT_CSV -> exportCsv();
            case EXIT, UNKNOWN -> { /* no-op */ }
        }
    }



    private void addGame() {
        System.out.print("Title: ");
        String title = sc.nextLine();

        System.out.print("Genre: ");
        String genre = sc.nextLine();

        System.out.print("Platform: ");
        String platform = sc.nextLine();

        System.out.print("Release year: ");
        Integer releaseYear = null;
        try {
            String yearStr = sc.nextLine();
            if (!yearStr.isBlank()) {
                releaseYear = Integer.parseInt(yearStr);
            }
        } catch (NumberFormatException ignored) {}

        String id = UUID.randomUUID().toString();
        Game g = new Game(id, title, genre, platform, releaseYear); // <-- под твой конструктор
        gameRepo.save(g);

        System.out.println("Saved game: " + g.getTitle() + " (" + g.getId() + ")");
    }



    private void addSession() {
        System.out.print("Game id: ");
        String gameId = sc.nextLine();

        if (gameRepo.findById(gameId).isEmpty()) {
            System.out.println("No game with id: " + gameId);
            return;
        }

        System.out.print("Minutes played: ");
        int minutes = Integer.parseInt(sc.nextLine());

        System.out.print("Character name (optional): ");
        String character = sc.nextLine();

        System.out.print("Notes (optional): ");
        String notes = sc.nextLine();

        PlaySession session = new PlaySession(
                gameId,
                java.time.LocalDateTime.now(), // текущая дата/время
                minutes,
                character,
                notes
        );

        sessionRepo.save(session);
        System.out.println("Session saved: " + session.getId());
    }


    private void showTopGames() {
        System.out.print("How many? ");
        int limit = Integer.parseInt(sc.nextLine());
        var top = stats.topGamesByTime(limit);
        System.out.println("Top by time: " + top);
    }

    private void showAverageSession() {
        var all = sessionRepo.findAll();
        double avg = all.isEmpty() ? 0.0 :
                all.stream().mapToInt(PlaySession::getMinutesPlayed).average().orElse(0.0);
        System.out.printf(Locale.US, "Average session length: %.2f minutes%n", avg);
    }

    private void showCountsByGenre() {
        var all = gameRepo.findAll();
        Map<String, Long> byGenre = all.stream()
                .collect(Collectors.groupingBy(
                        g -> {
                            try {
                                var m = g.getClass().getMethod("getGenre");
                                Object val = m.invoke(g);
                                return val == null || val.toString().isBlank() ? "unknown" : val.toString();
                            } catch (Exception e) {
                                return "unknown"; // если жанра нет в модели
                            }
                        },
                        Collectors.counting()
                ));
        System.out.println("Games by genre: " + byGenre);
    }


    private void listAllGames() {
        var games = gameRepo.findAll();
        if (games.isEmpty()) {
            System.out.println("No games yet.");
            return;
        }
        String fmt = "%-36s  %-24s  %-12s  %-12s  %-4s%n";
        System.out.printf(fmt, "ID", "Title", "Genre", "Platform", "Year");
        System.out.println("-".repeat(92));
        games.forEach(g -> System.out.printf(fmt,
                g.getId(),
                safe(g.getTitle()),
                safe(g.getGenre()),
                safe(g.getPlatform()),
                g.getReleaseYear()
        ));
    }
    private String safe(String s) { return (s == null || s.isBlank()) ? "-" : s; }



    private void listSessionsByGame() {
        System.out.print("Game id: ");
        String gameId = sc.nextLine();

        var gameOpt = gameRepo.findById(gameId);
        if (gameOpt.isEmpty()) {
            System.out.println("No game with id: " + gameId);
            return;
        }
        var sessions = sessionRepo.findByGameId(gameId);
        if (sessions.isEmpty()) {
            System.out.println("No sessions for: " + gameOpt.get().getTitle());
            return;
        }

        String fmt = "%-20s  %-19s  %-8s  %-18s  %-30s%n";
        System.out.printf("Sessions for %s:%n", gameOpt.get().getTitle());
        System.out.printf(fmt, "SessionID", "Start", "Minutes", "Character", "Notes");
        System.out.println("-".repeat(102));
        sessions.forEach(s -> System.out.printf(fmt,
                s.getId(),
                s.getStart(),
                s.getMinutesPlayed(),
                safe(s.getCharacterName()),
                truncate(s.getNotes(), 30)
        ));
    }
    private String truncate(String s, int max) {
        s = safe(s);
        return s.length() <= max ? s : s.substring(0, max - 1) + "…";
    }


    private void exportCsv() {
        try {
            java.nio.file.Path dir = java.nio.file.Path.of("export");
            java.nio.file.Files.createDirectories(dir);

            // games.csv
            var games = gameRepo.findAll();
            var gPath = dir.resolve("games.csv");
            try (var w = java.nio.file.Files.newBufferedWriter(gPath)) {
                w.write("id,title,genre,platform,releaseYear\n");
                for (var g : games) {
                    w.write(String.join(",",
                            csv(g.getId()),
                            csv(g.getTitle()),
                            csv(g.getGenre()),
                            csv(g.getPlatform()),
                            String.valueOf(g.getReleaseYear())
                    ));
                    w.write("\n");
                }
            }

            // sessions.csv
            var sessions = sessionRepo.findAll();
            var sPath = dir.resolve("sessions.csv");
            try (var w = java.nio.file.Files.newBufferedWriter(sPath)) {
                w.write("id,gameId,start,minutesPlayed,characterName,notes\n");
                for (var s : sessions) {
                    w.write(String.join(",",
                            csv(s.getId()),
                            csv(s.getGameId()),
                            csv(String.valueOf(s.getStart())),
                            String.valueOf(s.getMinutesPlayed()),
                            csv(s.getCharacterName()),
                            csv(s.getNotes())
                    ));
                    w.write("\n");
                }
            }

            System.out.println("Exported to " + dir.toAbsolutePath());
        } catch (Exception e) {
            System.out.println("Export failed: " + e.getMessage());
        }
    }
    private String csv(String v) {
        if (v == null) return "";
        String escaped = v.replace("\"", "\"\"");
        return "\"" + escaped + "\"";
    }


}
