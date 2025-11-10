package com.peppermode.tracker.cli;

import com.peppermode.tracker.repo.*;
import com.peppermode.tracker.repo.file.FileGameRepository;
import com.peppermode.tracker.repo.file.FileSessionRepository;
import com.peppermode.tracker.service.StatsService;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        boolean useFileStorage = true; // ← можешь переключать

        GameRepository gameRepo;
        SessionRepository sessionRepo;

        if (useFileStorage) {
            gameRepo = new FileGameRepository();
            sessionRepo = new FileSessionRepository();
            System.out.println("📁 Using JSON file storage (data/games.json, data/sessions.json)");
        } else {
            gameRepo = new InMemoryGameRepository();
            sessionRepo = new InMemorySessionRepository();
            System.out.println("💾 Using in-memory storage (temporary data)");
        }

        StatsService stats = new StatsService(gameRepo, sessionRepo);
        Scanner sc = new Scanner(System.in);
        CommandHandler handler = new CommandHandler(gameRepo, sessionRepo, stats, sc);

        System.out.println("=== PepperMode Game Progress Tracker ===");
        while (true) {
            System.out.println("""
                        1) Add game
                        2) Add session
                        3) Show top games
                        4) Show average session length
                        5) Games count by genre
                        6) List all games
                        7) List sessions by game
                        0) Exit
                    """);

            System.out.print("Choose: ");
            Command cmd = Command.from(sc.nextLine());
            if (cmd == Command.EXIT) break;
            handler.handle(cmd);
            System.out.println();
        }

        System.out.println("Bye!");
    }
}
