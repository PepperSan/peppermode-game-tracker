package com.peppermode.tracker.api;

import com.peppermode.tracker.api.dto.GameDto;
import com.peppermode.tracker.api.dto.SessionDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class SessionControllerIT {

    @LocalServerPort
    int port;

    @Autowired
    TestRestTemplate rest;

    private String url(String path) {
        return "http://localhost:" + port + path;
    }

    @Test
    void create_and_list_sessions() {
        // 1. Создаём игру, к которой привяжем сессию
        GameDto createGameReq = new GameDto(
                null,
                "Session Test Game",
                "RPG",
                "PC",
                2024
        );

        ResponseEntity<GameDto> createGameResp =
                rest.postForEntity(url("/api/games"), createGameReq, GameDto.class);

        assertThat(createGameResp.getStatusCode()).isEqualTo(HttpStatus.OK);
        GameDto game = createGameResp.getBody();
        assertThat(game).isNotNull();
        assertThat(game.id()).isNotNull();

        // 2. Создаём сессию для этой игры
        // В SessionDto поля:
        // String id, @NotBlank String gameId, LocalDateTime start,
        // @Min(1) int minutesPlayed, String characterName, String notes
        SessionDto createSessionReq = new SessionDto(
                null,
                game.id(),
                null,        // start (null -> внутри проставится now)
                10,          // minutesPlayed (>=1 из-за @Min(1))
                "Test Hero",
                "Test notes"
        );

        ResponseEntity<SessionDto> createSessionResp =
                rest.postForEntity(url("/api/sessions"), createSessionReq, SessionDto.class);

        assertThat(createSessionResp.getStatusCode()).isEqualTo(HttpStatus.OK);
        SessionDto createdSession = createSessionResp.getBody();
        assertThat(createdSession).isNotNull();
        assertThat(createdSession.id()).isNotNull();

        // 3. Получаем список всех сессий
        ResponseEntity<SessionDto[]> listResp =
                rest.getForEntity(url("/api/sessions"), SessionDto[].class);

        assertThat(listResp.getStatusCode()).isEqualTo(HttpStatus.OK);

        List<SessionDto> sessions = Arrays.asList(listResp.getBody());

        // 4. Проверяем, что наша сессия есть в списке
        assertThat(sessions)
                .extracting(SessionDto::id)
                .contains(createdSession.id());
    }
}
