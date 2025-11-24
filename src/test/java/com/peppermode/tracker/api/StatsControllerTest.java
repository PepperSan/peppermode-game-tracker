package com.peppermode.tracker.api;

import com.peppermode.tracker.PepperModeApplication;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(classes = PepperModeApplication.class)
@AutoConfigureMockMvc
class StatsControllerTest {

    @Autowired
    MockMvc mvc;

    String gameId1;
    String gameId2;

    @BeforeEach
    void setup() throws Exception {
        gameId1 = createGame("Game A", "RPG");
        gameId2 = createGame("Game B", "Action");

        createSession(gameId1, 40);
        createSession(gameId1, 20);
        createSession(gameId2, 10);
    }

    // --- helper: create game ---
    private String createGame(String title, String genre) throws Exception {
        String body = """
        {
          "title": "%s",
          "genre": "%s",
          "platform": "PC",
          "releaseYear": 2025
        }
        """.formatted(title, genre);

        var result = mvc.perform(post("/api/games")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isOk())
                .andReturn();

        var json = result.getResponse().getContentAsString();
        int start = json.indexOf("\"id\"");
        int q1 = json.indexOf('"', start + 4);
        int q2 = json.indexOf('"', q1 + 1);
        return json.substring(q1 + 1, q2);
    }

    // --- helper: create session ---
    private void createSession(String gameId, int minutes) throws Exception {
        String body = """
        {
          "gameId": "%s",
          "minutesPlayed": %d
        }
        """.formatted(gameId, minutes);

        mvc.perform(post("/api/sessions")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isOk());
    }

    @Test
    void shouldReturnGameStats() throws Exception {
        mvc.perform(get("/api/stats/games"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].totalMinutes").exists());
    }

    @Test
    void shouldReturnTopGames() throws Exception {
        mvc.perform(get("/api/stats/games/top?limit=1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].totalMinutes").exists());
    }

    @Test
    void shouldReturnAverageSessionLength() throws Exception {
        mvc.perform(get("/api/stats/sessions/average"))
                .andExpect(status().isOk());
    }

    @Test
    void shouldReturnGenresStats() throws Exception {
        mvc.perform(get("/api/stats/genres-count"))
                .andExpect(status().isOk());

    }

}
