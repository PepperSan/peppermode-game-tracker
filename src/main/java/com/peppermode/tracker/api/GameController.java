package com.peppermode.tracker.api;

import com.peppermode.tracker.api.dto.GameDto;
import com.peppermode.tracker.domain.Game;
import com.peppermode.tracker.repo.GameRepository;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;




import java.util.List;
import com.peppermode.tracker.service.GameService;
import java.util.NoSuchElementException;
import java.util.UUID;

@RestController
@RequestMapping("/api/games")
public class GameController {

    private final GameRepository games;
    private final GameService gameService;


    public GameController(GameRepository games, GameService gameService) {
        this.games = games;
        this.gameService = gameService;
    }


    @GetMapping
    public List<GameDto> all(
            @RequestParam(required = false) String genre,
            @RequestParam(required = false) String platform
    ) {
        var filtered = gameService.findGames(genre, platform);
        return filtered.stream()
                .map(GameDto::from)
                .toList();
    }



    @PostMapping
    public ResponseEntity<GameDto> create(@RequestBody @Valid GameDto dto) {
        String id = UUID.randomUUID().toString();
        Game saved = games.save(dto.toDomainNew(id));
        return ResponseEntity.ok(GameDto.from(saved));
    }
    @GetMapping("/{id}")
    public GameDto getById(@PathVariable String id) {
        var game = games.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Game not found"));

        return GameDto.from(game);
    }




}
