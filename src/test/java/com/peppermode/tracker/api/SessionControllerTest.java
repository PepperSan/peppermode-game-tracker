package com.peppermode.tracker.api;

import com.peppermode.tracker.PepperModeApplication;
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
class SessionControllerTest {

    @Autowired
    MockMvc mvc;

    @Test
    void shouldCreateSession() throws Exception {
        // сначала создаём игру, чтобы был валидный gameId
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
                .andReturn();

        String responseBody = result.getResponse().getContentAsString();

        // очень простой способ вытащить id без ObjectMapper
        // "id":"...."
        int idIndex = responseBody.indexOf("\"id\"");
        int colon = responseBody.indexOf(':', idIndex);
        int firstQuote = responseBody.indexOf('"', colon + 1);
        int secondQuote = responseBody.indexOf('"', firstQuote + 1);
        String gameId = responseBody.substring(firstQuote + 1, secondQuote);

        String sessionJson = """
        {
          "gameId": "%s",
          "minutesPlayed": 45
        }
        """.formatted(gameId);

        mvc.perform(post("/api/sessions")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(sessionJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.minutesPlayed").value(45));
    }

    @Test
    void shouldReturnSessionsList() throws Exception {
        mvc.perform(get("/api/sessions"))
                .andExpect(status().isOk());
    }
}

