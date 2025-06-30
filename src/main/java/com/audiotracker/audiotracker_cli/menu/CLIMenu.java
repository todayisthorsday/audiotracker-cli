package com.audiotracker.audiotracker_cli.menu;

import com.audiotracker.audiotracker_cli.client.ApiClient;

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
        System.out.println("Welcome to the Audiobook Tracker!");
        System.out.println("1. Total listening by user");
        System.out.println("2. All audiobooks read by user");
        System.out.println("3. All audiobooks by genre");
        System.out.println("4. All sessions by audiobook");
        System.out.println("5. Most recent sessions");
        System.out.println("0. Exit");
        System.out.println("Please select an option:");
    }
    private void viewAllAudiobooks() {
        System.out.println(apiClient.getAllAudiobooks());
    }

    private void totalListeningByUser() {
        System.out.print("Enter the user ID: ");
        Long userID = Long.parseLong(scanner.nextLine());
        String response = ApiClient.get("/sessions/user/" + userID + "/total-time");
        System.out.println("Total minutes listened: " + response);
    }

    private void allAudiobooksByUser() {
        System.out.print("Enter the user ID: ");
        Long userId = Long.parseLong(scanner.nextLine());
        String response = ApiClient.get("/sessions/user/" + userId);
        System.out.println("Audiobooks" + userId + "has listened to:\n" + response);
    }

    private void allAudiobooksByGenre() {
        System.out.print("Enter genre ID: ");
        Long genreId = Long.parseLong(scanner.nextLine());
        String response = ApiClient.get("/audiobooks/genre/" + genreId);
        System.out.println("Audiobooks in genre:\n" + response);
    }

    private void allSessionsByAudiobook() {
        System.out.print("Enter audiobook ID: ");
        Long audiobookId = Long.parseLong(scanner.nextLine());
        String response = ApiClient.get("/sessions/audiobook/" + audiobookId);
        System.out.println("Sessions for " + audiobookId + ":\n" + response);
    }

    private void mostRecentSessions() {
        String response = ApiClient.get("/sessions/most-recent");
        System.out.println("Most recent sessions:\n" + response);
    }
}

