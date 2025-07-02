
package com.audiotracker.audiotracker_cli.menu;

import com.audiotracker.audiotracker_cli.client.ApiClient;
import com.audiotracker.audiotracker_cli.model.Audiobook;
import com.audiotracker.audiotracker_cli.model.Genre;
import com.audiotracker.audiotracker_cli.model.Session;
import com.audiotracker.audiotracker_cli.model.User;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.eq;

public class CLIMenuTest {

    private ApiClient mockApiClient;
    private CLIMenu cliMenu;

    @BeforeEach
    public void setUp() {
        mockApiClient = Mockito.mock(ApiClient.class);
    }

    @Test
    public void testTotalListeningByUser_DisplaysCorrectTime() throws Exception {
        // Simulate "1" (menu option), then "1" (user ID), then enter to return
        String input = "1\n1\n\n0\n";
        System.setIn(new ByteArrayInputStream(input.getBytes()));

        Mockito.when(mockApiClient.getUserById(eq(1L)))
                .thenReturn("{\"id\":1,\"username\":\"Melanie\"}");

        Mockito.when(ApiClient.get("/sessions/user/1/total-time"))
                .thenReturn("90");

        cliMenu = new CLIMenu(mockApiClient);
        cliMenu.start();
    }

    @Test
    public void testAllAudiobooksByUser_ParsesAndPrintsCorrectly() throws Exception {
        String input = "2\n1\n\n0\n";
        System.setIn(new ByteArrayInputStream(input.getBytes()));

        Mockito.when(mockApiClient.getUserById(1L))
                .thenReturn("{\"id\":1,\"username\":\"Melanie\"}");

        Mockito.when(ApiClient.get("/audiobooks/user/1"))
                .thenReturn("[{\"id\":1,\"title\":\"Book Title\",\"author\":\"Author Name\",\"duration\":60,\"narrator\":\"Narrator\"}]");

        cliMenu = new CLIMenu(mockApiClient);
        cliMenu.start();
    }

    @Test
    public void testAllAudiobooksByGenre_ParsesCorrectly() throws Exception {
        String input = "3\n1\n\n0\n";
        System.setIn(new ByteArrayInputStream(input.getBytes()));

        Mockito.when(mockApiClient.getGenreById(1L))
                .thenReturn("{\"id\":1,\"name\":\"Sci-Fi\"}");

        Mockito.when(ApiClient.get("/audiobooks/genre/1"))
                .thenReturn("[{\"id\":2,\"title\":\"Another Book\",\"author\":\"Another Author\",\"duration\":45,\"narrator\":\"Narrator\"}]");

        cliMenu = new CLIMenu(mockApiClient);
        cliMenu.start();
    }

    @Test
    public void testAllSessionsByAudiobook_ParsesCorrectly() throws Exception {
        String input = "4\n1\n\n0\n";
        System.setIn(new ByteArrayInputStream(input.getBytes()));

        Mockito.when(ApiClient.get("/audiobooks/1"))
                .thenReturn("{\"id\":1,\"title\":\"Book One\",\"author\":\"Author\",\"duration\":60,\"narrator\":\"Narrator\"}");

        Mockito.when(ApiClient.get("/sessions/audiobook/1"))
                .thenReturn("[{\"id\":1,\"date\":\"2025-06-01\",\"length\":90,\"audiobookId\":1}]");

        cliMenu = new CLIMenu(mockApiClient);
        cliMenu.start();
    }

    @Test
    public void testMostRecentSessions_ParsesCorrectly() throws Exception {
        String input = "5\n\n0\n";
        System.setIn(new ByteArrayInputStream(input.getBytes()));

        Mockito.when(ApiClient.get("/sessions/recent?count=5"))
                .thenReturn("[{\"id\":1,\"date\":\"2025-06-29\",\"length\":120,\"audiobookId\":1}]");

        cliMenu = new CLIMenu(mockApiClient);
        cliMenu.start();
    }
}
