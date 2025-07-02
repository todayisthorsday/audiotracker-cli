package com.audiotracker.audiotracker_cli.menu;

import com.audiotracker.audiotracker_cli.client.ApiClient;
import com.audiotracker.audiotracker_cli.model.Audiobook;
import com.audiotracker.audiotracker_cli.model.Session;
import com.audiotracker.audiotracker_cli.model.User;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.Objects;
import java.util.Scanner;

public class CLIMenu {

    private final ApiClient apiClient;
    private final Scanner scanner;

    public CLIMenu() {
        this.apiClient = new ApiClient();
        this.scanner = new Scanner(System.in);
    }

    public void start() {
        boolean running = true;
        while (running) {
            printMainMenu();
            String input = scanner.nextLine();

            switch (input) {
                case "1" -> totalListeningByUser();
                case "2" -> allAudiobooksByUser();
                case "3" -> allAudiobooksByGenre();
                case "4" -> allSessionsByAudiobook();
                case "5" -> mostRecentSessions();
                case "0" -> running = false;
                default -> System.out.println("Invalid choice, please try again.");
            }
        }

        System.out.println("See you later, aligator!");
    }

    private void printMainMenu() {
        System.out.print("\033[H\033[2J");
        System.out.flush();

        System.out.println("🎧 Welcome to the Audiobook Tracker!");
        System.out.println();
        System.out.println("1 - 📖 Total listening by user");
        System.out.println("2 - 📚 All audiobooks read by user");
        System.out.println("3 - 🗂 All audiobooks by genre");
        System.out.println("4 - 🕒 All sessions by audiobook");
        System.out.println("5 - 📆 Most recent sessions");
        System.out.println("0 - ⏏️ Exit");
        System.out.println();
        System.out.println("Please select an option:");
    }

    private void viewAllAudiobooks() {
        System.out.println(apiClient.getAllAudiobooks());
    }

    // Menu Option 1
    private void totalListeningByUser() {
        System.out.print("Enter the user ID: ");
        Long userId = Long.parseLong(scanner.nextLine());
        String response = ApiClient.get("/sessions/user/" + userId + "/total-time");
        String userJson = apiClient.getUserById(userId);

        try {
            ObjectMapper mapper = new ObjectMapper();
            User user = mapper.readValue(userJson, User.class);

            int minutes = Integer.parseInt(response.trim());
            int hours = minutes / 60;
            int mins = minutes % 60;

            System.out.println(user.getUsername() + " has listened for: " + hours + " hours and " + mins + " minutes.");
        } catch (Exception e) {
            System.out.println("Unexpected response: " + response);
            System.out.println("Raw userJson: " + userJson);
            System.out.println("Raw response: " + response);
            System.out.println("Error: " + e.getMessage());
        }

        System.out.println("\nPress Enter to return to the main menu...");
        scanner.nextLine();
    }

    // Menu Option 2
    private void allAudiobooksByUser() {
        System.out.print("Enter the user ID: ");
        Long userId = Long.parseLong(scanner.nextLine());
        String response = ApiClient.get("/audiobooks/user/" + userId);

        try {
            ObjectMapper mapper = new ObjectMapper();

            String userJson = apiClient.getUserById(userId);
            User user = mapper.readValue(userJson, User.class);

            Audiobook[] audiobooks = mapper.readValue(response, Audiobook[].class);

            System.out.println("\nAudiobooks read by " + user.getUsername() + ":\n");

            if (audiobooks.length == 0) {
                System.out.println("This user hasn't read any audiobooks yet.");
            } else {
                for (Audiobook book : audiobooks) {
                    System.out.println("- \"" + book.getTitle() + "\" by " + book.getAuthor()
                            + " (Narrated by " + book.getNarrator() + ", " + book.getDuration() + " mins).");
                }
            }

        } catch (Exception e) {
            System.out.println("Failed to parse response: " + e.getMessage());
            System.out.println("Raw response:\n" + response);
        }

        System.out.println("\nPress Enter to return to the main menu...");
        scanner.nextLine();
    }

    // Menu Option 3
    private void allAudiobooksByGenre() {
        System.out.print("Enter genre ID: ");
        Long genreId = Long.parseLong(scanner.nextLine());
        String response = ApiClient.get("/audiobooks/genre/" + genreId);
        String genreJson = apiClient.getGenreById(genreId);

        try {
            ObjectMapper mapper = new ObjectMapper();
            com.audiotracker.audiotracker_cli.model.Genre genre =
                    mapper.readValue(genreJson, com.audiotracker.audiotracker_cli.model.Genre.class);

            Audiobook[] audiobooks = mapper.readValue(response, Audiobook[].class);

            System.out.println("Audiobooks in genre \"" + genre.getName() + "\":\n");

            if (audiobooks.length == 0) {
                System.out.println("No audiobooks found in this genre.");
            } else {
                for (Audiobook book : audiobooks) {
                    System.out.println("- \"" + book.getTitle() + "\" by " + book.getAuthor()
                            + " (Narrated by " + book.getNarrator() + ", " + book.getDuration() + " mins).");
                }
            }

        } catch (Exception e) {
            System.out.println("Failed to parse response: " + e.getMessage());
            System.out.println("Raw response:\n" + response);
        }

        System.out.println("\nPress Enter to return to the main menu...");
        scanner.nextLine();
    }

    // Menu Option 4
    private void allSessionsByAudiobook() {
        System.out.print("Enter audiobook ID: ");
        Long audiobookId = Long.parseLong(scanner.nextLine());

        String bookJson = ApiClient.get("/audiobooks/" + audiobookId);
        String response = ApiClient.get("/sessions/audiobook/" + audiobookId);

        try {
            ObjectMapper mapper = new ObjectMapper();
            Audiobook book = mapper.readValue(bookJson, Audiobook.class);
            Session[] sessions = mapper.readValue(response, Session[].class);

            if (sessions.length == 0) {
                System.out.println("No sessions found for \"" + book.getTitle()  + "\".");
            } else {
                System.out.println("Listening sessions for \"" + book.getTitle() + "\":");
                for (Session s : sessions) {
                    int hours = s.getLength() / 60;
                    int mins = s.getLength() % 60;
                    System.out.println("- " + s.getDate() + ": " + hours + " hours and " + mins + " minutes.");
                }
            }

        } catch (Exception e) {
            System.out.println("Failed to parse response.");
            System.out.println("Raw JSON:\n" + response);
            System.out.println("Error: " + e.getMessage());
        }

        System.out.println("\nPress Enter to return to the main menu...");
        scanner.nextLine();
    }

    // Menu Option 5
    private void mostRecentSessions() {
        String response = ApiClient.get("/sessions/recent?count=5");

        try {
            ObjectMapper mapper = new ObjectMapper();
            Session[] sessions = mapper.readValue(response, Session[].class);

            if (sessions.length == 0) {
                System.out.println("No recent sessions found.");
            } else {
                System.out.println("\nMost recent sessions:");

                for (Session s : sessions) {
                    int hours = s.getLength() / 60;
                    int mins = s.getLength() % 60;

                    System.out.println("- " + s.getDate() + " | "  + hours + "h " + mins + " m");
                }
            }

        } catch (Exception e) {
            System.out.println("Could not load sessions:\n" + response);
            System.out.println("Error: " + e.getMessage());
            System.out.println("Raw response: " + response);
        }

        System.out.println("\nPress Enter to return to the main menu...");
        scanner.nextLine();
    }
}

