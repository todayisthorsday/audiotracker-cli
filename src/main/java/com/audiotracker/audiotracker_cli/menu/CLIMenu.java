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
                case "1" -> viewAllAudiobooks();
                case "2" -> viewAllGenres();
                case "3" -> viewAllUsers();
                case "4" -> viewAllSessions();
                case "0" -> running = false;
                default -> System.out.println("Invalid choice, please try again.");
            }
        }

        System.out.println("Goodbye!");
    }

    private void printMainMenu() {
        System.out.println("Welcome to the Audiobook Tracker!");
        System.out.println("1. View all audiobooks");
        System.out.println("2. View all genres");
        System.out.println("3. View all users");
        System.out.println("4. View all sessions");
        System.out.println("0. Exit");
        System.out.println("Please select an option:");
    }
    private void viewAllAudiobooks() {
        System.out.println(apiClient.getAllAudiobooks());
    }

    private void viewAllGenres() {
        System.out.println(apiClient.getAllGenres());
    }

    private void viewAllUsers() {
        System.out.println(apiClient.getAllUsers());
    }

    private void viewAllSessions() {
        System.out.println(apiClient.getAllSessions());
    }
}

