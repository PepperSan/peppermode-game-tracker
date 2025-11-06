package com.peppermode.tracker.cli;

import com.peppermode.tracker.repo.*;
import com.peppermode.tracker.service.StatsService;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        GameRepository gameRepo = new InMemoryGameRepository();
        SessionRepository sessionRepo = new InMemorySessionRepository();
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
                    8) Export CSV (games & sessions)
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
