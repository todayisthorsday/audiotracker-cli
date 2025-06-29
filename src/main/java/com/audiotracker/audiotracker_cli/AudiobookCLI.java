package com.audiotracker.audiotracker_cli;

import com.audiotracker.audiotracker_cli.client.ApiClient;
import org.json.JSONArray;
import org.json.JSONObject;

public class AudiobookCLI {

    private final ApiClient apiClient;

    public AudiobookCLI() {
        this.apiClient = new ApiClient();
    }

    public void displayAllAudiobooks() {
        try {
            String response = apiClient.getAllAudiobooks();
            JSONArray books = new JSONArray(response);

            System.out.println("\n🎧All Audiobooks:");
            for (int i = 0; i < books.length(); i++) {
                JSONObject book = books.getJSONObject(i);
                System.out.println("- " + book.getString("title") +
                        " by " + book.getString("author") +
                        " (" + book.getInt("duration") + " min)" +
                        " | Genre: " + book.getJSONObject("genre").getString("name"));
            }
        } catch (Exception e) {
            System.err.println("Failed to fetch all audiobooks: " + e.getMessage());
        }

    }
}
