package com.peppermode.tracker.api;

import com.peppermode.tracker.api.dto.SessionDto;
import com.peppermode.tracker.repo.SessionRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@Tag(name = "Games API", description = "Операции с играми")
@RestController
@RequestMapping("/api/sessions")
public class SessionController {

    private final SessionRepository sessions;

    public SessionController(SessionRepository sessions) {
        this.sessions = sessions;
    }

    // 7. Получить все сессии
    @Operation(
            summary = "Получить все сессии",
            description = "Возвращает список всех игровых сессий."
    )
    @ApiResponse(responseCode = "200", description = "Список сессий получен")
    @GetMapping
    public List<SessionDto> getAll() {
        return sessions.findAll().stream()
                .map(SessionDto::from)
                .toList();
    }

    // 8. Получить сессии по ID игры
    @Operation(
            summary = "Получить сессии по игре",
            description = "Возвращает все сессии для указанной игры."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Список сессий получен"),
            @ApiResponse(responseCode = "404", description = "Игра не найдена или сессий нет")
    })
    @GetMapping("/game/{gameId}")
    public List<SessionDto> getByGame(@PathVariable String gameId) {
        return sessions.findByGameId(gameId).stream()
                .map(SessionDto::from)
                .toList();
    }

    // 9. Получить сессию по ID
    @Operation(
            summary = "Получить сессию по ID",
            description = "Возвращает одну сессию по её идентификатору."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Сессия найдена"),
            @ApiResponse(responseCode = "404", description = "Сессия не найдена")
    })
    @GetMapping("/{id}")
    public SessionDto getById(@PathVariable String id) {
        var session = sessions.findById(id)
                .orElseThrow(() -> new RuntimeException("Session not found: " + id));
        return SessionDto.from(session);
    }

    @Operation(
            summary = "Создать игровую сессию",
            description = "Создаёт новую сессию для указанной игры."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Сессия создана"),
            @ApiResponse(responseCode = "400", description = "Невалидные данные сессии")
    })
    @PostMapping
    public ResponseEntity<SessionDto> create(@RequestBody @Valid SessionDto dto) {
        var saved = sessions.save(dto.toDomainNew());
        return ResponseEntity.ok(SessionDto.from(saved));
    }

    // 10. Удалить сессию по ID
    @Operation(
            summary = "Удалить сессию",
            description = "Удаляет сессию по ID."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Сессия удалена"),
            @ApiResponse(responseCode = "404", description = "Сессия не найдена")
    })
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteById(@PathVariable String id) {
        sessions.deleteById(id);
    }

    // 11. Удалить ВСЕ сессии
    @Operation(
            summary = "Удалить все сессии",
            description = "Очищает хранилище игровых сессий."
    )
    @ApiResponse(responseCode = "204", description = "Все сессии удалены")
    @DeleteMapping
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteAll() {
        sessions.deleteAll();
    }
}

