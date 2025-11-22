package com.peppermode.tracker.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.peppermode.tracker.PepperModeApplication;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(classes = PepperModeApplication.class)
@AutoConfigureMockMvc
class SessionControllerTest {

    @Autowired
    MockMvc mvc;

    private final ObjectMapper objectMapper = new ObjectMapper();

    /**
     * Вспомогательный метод — создаём тестовую игру и возвращаем её id.
     */
    private String createTestGame() throws Exception {
        String gameJson = """
            {
              "title": "Session Test Game",
              "genre": "RPG",
              "platform": "PC",
              "releaseYear": 2025
            }
            """;

        var result = mvc.perform(post("/api/games")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(gameJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").exists())
                .andReturn();

        String body = result.getResponse().getContentAsString();
        return objectMapper.readTree(body).get("id").asText();
    }

    /**
     * Вспомогательный метод — создаём сессию для игры.
     */
    private void createSession(String gameId, int minutesPlayed) throws Exception {
        String sessionJson = """
            {
              "gameId": "%s",
              "minutesPlayed": %d
            }
            """.formatted(gameId, minutesPlayed);

        mvc.perform(post("/api/sessions")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(sessionJson))
                .andExpect(status().isOk());
    }

    @Test
    void shouldReturnSessionsList() throws Exception {
        mvc.perform(get("/api/sessions"))
                .andExpect(status().isOk());
    }

    @Test
    void shouldCreateSession() throws Exception {
        // given: есть игра, берём её id
        String gameId = createTestGame();

        String sessionJson = """
            {
              "gameId": "%s",
              "minutesPlayed": 45
            }
            """.formatted(gameId);

        // when + then
        mvc.perform(post("/api/sessions")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(sessionJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.gameId").value(gameId))
                .andExpect(jsonPath("$.minutesPlayed").value(45));
    }

    @Test
    void shouldReturnSessionsByGameId() throws Exception {
        // given
        String gameId = createTestGame();
        createSession(gameId, 30);
        createSession(gameId, 45);

        // when + then
        mvc.perform(get("/api/sessions/game/{gameId}", gameId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].gameId").value(gameId))
                .andExpect(jsonPath("$[1].gameId").value(gameId));
    }
}
