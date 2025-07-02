package com.audiotracker.audiotracker_cli.menu;

import com.audiotracker.audiotracker_cli.client.ApiClient;
import org.junit.jupiter.api.*;
import org.mockito.MockedStatic;
import org.mockito.Mockito;

import java.io.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

/**
 * Unit tests for the CLIMenu class covering each menu option.
 */
public class CLIMenuTest {

    private final PrintStream originalOut = System.out;
    private final InputStream originalIn = System.in;
    private ByteArrayOutputStream outContent;

    @BeforeEach
    void setUpStreams() {
        // Capture System.out output
        outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));
    }

    @AfterEach
    void restoreStreams() {
        // Restore original System.in and System.out
        System.setOut(originalOut);
        System.setIn(originalIn);
    }

    @Test
    void testExitOption() {
        // Simulate user entering "0" to exit immediately
        System.setIn(new ByteArrayInputStream("0\n".getBytes()));
                CLIMenu menu = new CLIMenu(mock(ApiClient.class));
        menu.start();
        String output = outContent.toString();
        // Verify welcome message appears
        assertTrue(output.contains("Welcome to the Audiobook Tracker"));
    }

    @Test
    void testInvalidChoice() {
        // Simulate invalid choice "9" then exit
        System.setIn(new ByteArrayInputStream("9\n\n0\n".getBytes()));
                CLIMenu menu = new CLIMenu(mock(ApiClient.class));
        menu.start();
        String output = outContent.toString();
        // Verify invalid choice prompt
        assertTrue(output.contains("Invalid choice, please try again."));
    }

    @Test
    void testTotalListeningByUser() {
        // Prepare static mock for ApiClient.get (session total) and instance mock for getUserById
        try (MockedStatic<ApiClient> staticMock = Mockito.mockStatic(ApiClient.class)) {
            staticMock.when(() -> ApiClient.get("/sessions/user/123/total-time")).thenReturn("125");
            ApiClient clientMock = mock(ApiClient.class);
            when(clientMock.getUserById(123L)).thenReturn("""
                    {"id":123,"username":"testuser"}""");

            // Simulate selecting option 1, entering user ID 123, pressing Enter, then exiting
            System.setIn(new ByteArrayInputStream("1\n123\n\n0\n".getBytes()));
                    CLIMenu menu = new CLIMenu(clientMock);
            menu.start();

            String output = outContent.toString();
            // Check that the formatted total listening output appears
            assertTrue(output.contains("testuser has listened for: 2 hours and 5 minutes."));
        }
    }

    @Test
    void testAllAudiobooksByUser() {
        // Mock static API call and instance user fetch
        String audiobooksJson = "[{\"id\":1,\"title\":\"Book One\",\"author\":\"Author A\",\"duration\":60,"
                + "\"narrator\":\"Narrator X\",\"genre\":{\"id\":10,\"name\":\"Genre1\"}}]";        try (MockedStatic<ApiClient> staticMock = Mockito.mockStatic(ApiClient.class)) {
            staticMock.when(() -> ApiClient.get("/audiobooks/user/123")).thenReturn(audiobooksJson);
            ApiClient clientMock = mock(ApiClient.class);
            when(clientMock.getUserById(123L)).thenReturn("""
            {"id":123,"username":"reader"}""");

            System.setIn(new ByteArrayInputStream("2\n123\n\n0\n".getBytes()));
                    CLIMenu menu = new CLIMenu(clientMock);
            menu.start();

            String output = outContent.toString();
            // Verify that the audiobooks list by user is printed
            assertTrue(output.contains("Audiobooks read by reader"));
            assertTrue(output.contains("\"Book One\" by Author A"));
        }
    }

    @Test
    void testAllAudiobooksByGenre() {
        // Mock static API call and instance genre fetch
        String genreJson = """
        {"id":5,"name":"Fiction"}""";
        String audiobooksJson = "[{\"id\":2,\"title\":\"Novel Two\",\"author\":\"Author B\",\"duration\":120,"
                + "\"narrator\":\"Narrator Y\",\"genre\":{\"id\":5,\"name\":\"Fiction\"}}]";
        try (MockedStatic<ApiClient> staticMock = Mockito.mockStatic(ApiClient.class)) {
            staticMock.when(() -> ApiClient.get("/audiobooks/genre/5")).thenReturn(audiobooksJson);
            ApiClient clientMock = mock(ApiClient.class);
            when(clientMock.getGenreById(5L)).thenReturn(genreJson);

            System.setIn(new ByteArrayInputStream("3\n5\n\n0\n".getBytes()));
                    CLIMenu menu = new CLIMenu(clientMock);
            menu.start();

            String output = outContent.toString();
            // Verify genre listing and audiobook info
            assertTrue(output.contains("Audiobooks in genre \"Fiction\""));
            assertTrue(output.contains("\"Novel Two\" by Author B"));
        }
    }

    @Test
    void testMostRecentSessions() {
        // Mock static API call for recent sessions
        String sessionsJson = """
        [{"id":100,"date":"2025-07-01","length":90,"audiobookId":2}]""";
        try (MockedStatic<ApiClient> staticMock = Mockito.mockStatic(ApiClient.class)) {
            staticMock.when(() -> ApiClient.get("/sessions/recent?count=5")).thenReturn(sessionsJson);

            System.setIn(new ByteArrayInputStream("5\n\n0\n".getBytes()));
                    CLIMenu menu = new CLIMenu(mock(ApiClient.class));
            menu.start();

            String output = outContent.toString();
            // Verify that session info is printed
            assertTrue(output.contains("Most recent sessions"));
            assertTrue(output.contains("1h 30 m")); // 90 minutes formatted
        }
    }
}
