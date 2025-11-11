package com.peppermode.tracker.api;

import com.peppermode.tracker.api.dto.SessionDto;
import com.peppermode.tracker.repo.SessionRepository;
import jakarta.validation.Valid;
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

    @GetMapping
    public List<SessionDto> byGame(@RequestParam(required = false) String gameId) {
        var list = (gameId == null || gameId.isBlank())
                ? sessions.findAll()
                : sessions.findByGameId(gameId);
        return list.stream().map(SessionDto::from).toList();
    }

    @PostMapping
    public ResponseEntity<SessionDto> create(@RequestBody @Valid SessionDto dto) {
        var saved = sessions.save(dto.toDomainNew());
        return ResponseEntity.ok(SessionDto.from(saved));
    }
}
