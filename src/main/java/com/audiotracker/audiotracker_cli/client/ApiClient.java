package com.audiotracker.audiotracker_cli.client;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.nio.charset.StandardCharsets;

public class ApiClient {
    private static final String BASE_URL = "http://localhost:8080/api";

    public static String get(String path) {
        return new ApiClient().sendGetRequest(path);
    }

    // GET endpoints
    public String getAllUsers() {
        return sendGetRequest("/users");
    }

    public String getAllAudiobooks() {
        return sendGetRequest("/audiobooks");
    }

    public String getAllGenres() {
        return sendGetRequest("/genres");
    }

    public String getAllSessions() {
        return sendGetRequest("/sessions");
    }

    // POST endpoints
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

    // PUT endpoints
    public String updateUser(Long id, String jsonPayload) {
        return sendPutRequest("/users/" + id, jsonPayload);
    }

    public String updateAudiobook(Long id, String jsonPayload) {
        return sendPutRequest("/audiobooks/" + id, jsonPayload);
    }

    public String updateGenre(Long id, String jsonPayload) {
        return sendPutRequest("/genres/" + id, jsonPayload);
    }

    public String updateSession(Long id, String jsonPayload) {
        return sendPutRequest("/sessions/" + id, jsonPayload);
    }

    // DELETE endpoints
    public String deleteUser(Long id) {
        return sendDeleteRequest("/users/" + id);
    }

    public String deleteAudiobook(Long id) {
        return sendDeleteRequest("/audiobooks/" + id);
    }

    public String deleteGenre(Long id) {
        return sendDeleteRequest("/genres/" + id);
    }

    public String deleteSession(Long id) {
        return sendDeleteRequest("/sessions/" + id);
    }

    // Internal helper
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

    private String sendPutRequest(String endpoint, String jsonInput) {
        try {
            URL url = URI.create(BASE_URL + endpoint).toURL();
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("PUT");
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

    private String sendDeleteRequest(String endpoint) {
        try {
            URL url = URI.create(BASE_URL + endpoint).toURL();
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("DELETE");

            int status = con.getResponseCode();
            if (status >= 400) return "Error: " + status;

            return "Deleted successfully.";
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