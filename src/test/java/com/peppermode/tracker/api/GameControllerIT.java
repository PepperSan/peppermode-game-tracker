package com.peppermode.tracker.api;

import com.peppermode.tracker.api.dto.GameDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.*;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class GameControllerIT {

    @LocalServerPort
    int port;

    @Autowired
    TestRestTemplate rest;

    private String url(String path) {
        return "http://localhost:" + port + path;
    }

    @Test
    void create_update_and_list_games() {
        // 1. Создаём игру
        GameDto createReq = new GameDto(
                null,
                "Test Game",
                "Action",
                "PC",
                2024
        );

        ResponseEntity<GameDto> createResp =
                rest.postForEntity(url("/api/games"), createReq, GameDto.class);

        assertThat(createResp.getStatusCode()).isEqualTo(HttpStatus.OK);
        GameDto created = createResp.getBody();
        assertThat(created).isNotNull();
        assertThat(created.id()).isNotNull();


// 2. Обновляем игру
        GameDto updateReq = new GameDto(
                created.id(),
                "Updated Test Game",
                created.genre(),
                created.platform(),
                created.releaseYear()
        );

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<GameDto> updateEntity = new HttpEntity<>(updateReq, headers);

        ResponseEntity<GameDto> updateResp =
                rest.exchange(url("/api/games/" + created.id()),
                        HttpMethod.PUT,
                        updateEntity,
                        GameDto.class);

        assertThat(updateResp.getStatusCode()).isEqualTo(HttpStatus.OK);
        GameDto updated = updateResp.getBody();
        assertThat(updated).isNotNull();
        assertThat(updated.title()).isEqualTo("Updated Test Game");


        // 3. Получаем список игр и убеждаемся, что обновлённая там есть
        ResponseEntity<GameDto[]> listResp =
                rest.getForEntity(url("/api/games"), GameDto[].class);

        assertThat(listResp.getStatusCode()).isEqualTo(HttpStatus.OK);

        List<GameDto> games = Arrays.asList(listResp.getBody());

        assertThat(games)
                .extracting(GameDto::id)
                .contains(created.id());

    }
}

