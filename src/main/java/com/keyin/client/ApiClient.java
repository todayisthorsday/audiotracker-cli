package com.keyin.client;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.nio.charset.StandardCharsets;

public class ApiClient {
    private static final String BASE_URL = "http://localhost:8080/api";

    public String getAllUsers() {
        return sendGetRequest("/users");
    }

    public String getAllAudiobooks() {
        return sendGetRequest("/audiobooks");
    }

    public String getAllSessions() {
        return sendGetRequest("/sessions");
    }

    public String createUser(String jsonPayload) {
        return sendPostRequest("/users", jsonPayload);
    }

    public String createAudiobook(String jsonPayload) {
        return sendPostRequest("/audiobooks", jsonPayload);
    }

    public String createGenre(String jsonPayload) {
        return sendPostRequest("/genres", jsonPayload);
    }

    public String createSession(String jsonPayload) {
        return sendPostRequest("/sessions", jsonPayload);
    }

    private String sendGetRequest(String endpoint) {
        try {
            URL url = URI.create(BASE_URL + endpoint).toURL();
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("GET");

            int status = con.getResponseCode();
            if (status != 200) {
                return "Error: " + status;
            }

            BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
            String inputLine;
            StringBuilder content = new StringBuilder();

            while ((inputLine = in.readLine()) != null) {
                content.append(inputLine).append("\n");
            }

            in.close();
            con.disconnect();
            return content.toString();

        } catch (Exception e) {
            return "Exception: " + e.getMessage();
        }
    }

    private String sendPostRequest(String endpoint, String jsonInput) {
        try {
            URL url = URI.create(BASE_URL + endpoint).toURL();
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("POST");
            con.setRequestProperty("Content-Type", "application/json");
            con.setDoOutput(true);

            try (OutputStream os = con.getOutputStream()) {
                byte[] input = jsonInput.getBytes(StandardCharsets.UTF_8);
                os.write(input, 0, input.length);
            }

            int status = con.getResponseCode();
            if (status >= 400) return "Error: " + status;

            return readResponse(con);

        } catch (Exception e) {
            return "Exception: " + e.getMessage();
        }
    }

    private String readResponse(HttpURLConnection con) throws IOException {
        BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream(), StandardCharsets.UTF_8));
        StringBuilder response = new StringBuilder();
        String line;
        while ((line = in.readLine()) != null) {
            response.append(line.trim()).append("\n");
        }
        in.close();
        con.disconnect();
        return response.toString();
        }
    }
