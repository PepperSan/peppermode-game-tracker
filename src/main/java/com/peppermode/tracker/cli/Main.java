package com.peppermode.tracker.cli;

import com.peppermode.tracker.domain.Game;
import com.peppermode.tracker.domain.PlaySession;
import com.peppermode.tracker.repo.*;
import com.peppermode.tracker.service.StatsService;

import java.time.LocalDateTime;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        GameRepository gameRepo = new InMemoryGameRepository();
        SessionRepository sessionRepo = new InMemorySessionRepository();
        StatsService stats = new StatsService(gameRepo, sessionRepo);

        Scanner sc = new Scanner(System.in);
        System.out.println("=== PepperMode Game Progress Tracker ===");

        while (true) {
            System.out.println("\n1) Add game");
            System.out.println("2) Add session");
            System.out.println("3) Show top games");
            System.out.println("4) Show average session length");
            System.out.println("5) Games count by genre");
            System.out.println("0) Exit");
            System.out.print("Choose: ");
            String choice = sc.nextLine();

            switch (choice) {
                case "1":
                    System.out.print("Title: "); String t = sc.nextLine();
                    System.out.print("Genre: "); String g = sc.nextLine();
                    System.out.print("Platform: "); String p = sc.nextLine();
                    System.out.print("Release year: "); int y = Integer.parseInt(sc.nextLine());
                    Game game = new Game(t, g, p, y);
                    gameRepo.save(game);
                    System.out.println("Saved: " + game.getId());
                    break;

                case "2":
                    System.out.print("Game ID: "); String gid = sc.nextLine();
                    System.out.print("Minutes played: "); int m = Integer.parseInt(sc.nextLine());
                    System.out.print("Character: "); String ch = sc.nextLine();
                    System.out.print("Notes: "); String notes = sc.nextLine();
                    sessionRepo.save(new PlaySession(gid, LocalDateTime.now(), m, ch, notes));
                    System.out.println("Session saved.");
                    break;

                case "3":
                    System.out.println("Top games by total playtime:");
                    stats.topGamesByTime(5).forEach(gm -> System.out.println("- " + gm));
                    break;

                case "4":
                    System.out.println("Average session: " + stats.averageSessionLengthMinutes() + " minutes");
                    break;

                case "5":
                    System.out.println("Games count by genre:");
                    stats.gamesCountByGenre().forEach((genre, count) ->
                            System.out.println(genre + ": " + count));
                    break;

                case "0":
                    System.out.println("Bye!");
                    return;

                default:
                    System.out.println("Unknown option");
            }
        }
    }
}
