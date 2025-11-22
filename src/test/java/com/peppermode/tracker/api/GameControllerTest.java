package com.peppermode.tracker.api;

import com.peppermode.tracker.PepperModeApplication;
import com.peppermode.tracker.api.dto.GameDto;
import com.peppermode.tracker.service.GameService;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;


import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(classes = PepperModeApplication.class)
@AutoConfigureMockMvc
class GameControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private GameService gameService;



    @Test
    void shouldCreateGame() throws Exception {
        String json = """
        {
          "title": "Test Game",
          "genre": "RPG",
          "platform": "PC",
          "releaseYear": 2025
        }
        """;

        mockMvc.perform(post("/api/games")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.title").value("Test Game"));
    }

    @Test
    void shouldReturnGamesList() throws Exception {
        mockMvc.perform(get("/api/games"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith("application/json"));
    }

    @Test
    void getAllGames_returns200AndList() throws Exception {
        var game1 = new GameDto(
                "1",
                "Ghost of Tsushima",
                "Action",
                "PS5",
                2020
        );

        var game2 = new GameDto(
                "2",
                "Cyberpunk 2077",
                "RPG",
                "PC",
                2025
        );

        when(gameService.findGames(any(), any(), any(), any(), any()))
                .thenReturn(List.of(
                        game1.toDomain(),
                        game2.toDomain()
                ));



        mockMvc.perform(get("/api/games"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].title").value("Ghost of Tsushima"))
                .andExpect(jsonPath("$[1].title").value("Cyberpunk 2077"));
    }

    @Test
    void shouldFilterGamesByGenreAndPlatform() throws Exception {
        // given
        var game1 = new GameDto("1", "Ghost of Tsushima", "Action", "PS5", 2020);
        var game2 = new GameDto("2", "Cyberpunk 2077", "RPG", "PC", 2020);

        when(gameService.findGames(
                eq("Action"),   // genre
                eq("PS5"),      // platform
                any(),          // yearFrom
                any(),          // yearTo
                any()           // sort
        )).thenReturn(List.of(
                game1.toDomain()
        ));

        // when + then
        mockMvc.perform(get("/api/games")
                        .param("genre", "Action")
                        .param("platform", "PS5"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].title").value("Ghost of Tsushima"))
                .andExpect(jsonPath("$.length()").value(1));
    }


}

