package com.peppermode.tracker.api;

import com.peppermode.tracker.api.dto.SessionDto;
import com.peppermode.tracker.repo.SessionRepository;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@RestController
@RequestMapping("/api/sessions")
public class SessionController {

    private final SessionRepository sessions;

    public SessionController(SessionRepository sessions) {
        this.sessions = sessions;
    }

    // 7. Получить все сессии
    @GetMapping
    public List<SessionDto> getAll() {
        return sessions.findAll().stream()
                .map(SessionDto::from)
                .toList();
    }

    // 8. Получить сессии по ID игры
    @GetMapping("/game/{gameId}")
    public List<SessionDto> getByGame(@PathVariable String gameId) {
        return sessions.findByGameId(gameId).stream()
                .map(SessionDto::from)
                .toList();
    }

    // 9. Получить сессию по ID
    @GetMapping("/{id}")
    public SessionDto getById(@PathVariable String id) {
        var session = sessions.findById(id)
                .orElseThrow(() -> new RuntimeException("Session not found: " + id));
        return SessionDto.from(session);
    }

    // Создать сессию
    @PostMapping
    public ResponseEntity<SessionDto> create(@RequestBody @Valid SessionDto dto) {
        var saved = sessions.save(dto.toDomainNew());
        return ResponseEntity.ok(SessionDto.from(saved));
    }

    // 10. Удалить сессию по ID
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteById(@PathVariable String id) {
        sessions.deleteById(id);
    }

    // 11. Удалить ВСЕ сессии
    @DeleteMapping
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteAll() {
        sessions.deleteAll();
    }
}

