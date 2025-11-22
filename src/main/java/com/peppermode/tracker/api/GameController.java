package com.peppermode.tracker.api;

import com.peppermode.tracker.api.dto.GameDto;
import com.peppermode.tracker.domain.Game;
import com.peppermode.tracker.repo.GameRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;




import java.util.List;
import com.peppermode.tracker.service.GameService;
import java.util.NoSuchElementException;
import java.util.UUID;

@Tag(name = "Games API", description = "Операции с играми")
@RestController
@RequestMapping("/api/games")
public class GameController {

    private final GameRepository games;
    private final GameService gameService;


    public GameController(GameRepository games, GameService gameService) {
        this.games = games;
        this.gameService = gameService;
    }
    @Operation(
            summary = "Получить список игр",
            description = "Возвращает все игры из хранилища с опциональными фильтрами по жанру, платформе и году."
    )
    @ApiResponse(
            responseCode = "200",
            description = "Список игр получен"
    )
    @GetMapping
    public List<GameDto> all(
            @RequestParam(required = false) String genre,
            @RequestParam(required = false) String platform,
            @RequestParam(required = false) Integer yearFrom,
            @RequestParam(required = false) Integer yearTo,
            @RequestParam(required = false) String sort
    ) {
        var filtered = gameService.findGames(genre, platform, yearFrom, yearTo, sort);

        return filtered.stream()
                .map(GameDto::from)
                .toList();
    }

    @Operation(
            summary = "Создать игру",
            description = "Добавляет новую игру в хранилище."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Игра создана"),
            @ApiResponse(responseCode = "400", description = "Невалидные данные игры")
    })
    @PostMapping
    public ResponseEntity<GameDto> create(@RequestBody @Valid GameDto dto) {
        String id = UUID.randomUUID().toString();
        Game saved = games.save(dto.toDomainNew(id));
        return ResponseEntity.ok(GameDto.from(saved));
    }

    @Operation(
            summary = "Получить игру по ID",
            description = "Возвращает игру по её идентификатору."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Игра найдена"),
            @ApiResponse(responseCode = "404", description = "Игра не найдена")
    })
    @GetMapping("/{id}")
    public GameDto getById(@PathVariable String id) {
        var game = games.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Game not found"));

        return GameDto.from(game);
    }

    @Operation(
            summary = "Удалить игру",
            description = "Удаляет игру по ID."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Игра удалена"),
            @ApiResponse(responseCode = "404", description = "Игра не найдена")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteById(@PathVariable String id) {
        games.deleteById(id);
        return ResponseEntity.noContent().build(); // 204 No Content
    }

    @Operation(
            summary = "Удалить все игры",
            description = "Очищает хранилище игр."
    )
    @ApiResponse(responseCode = "204", description = "Все игры удалены")
    @DeleteMapping
    public ResponseEntity<Void> deleteAll() {
        games.deleteAll();
        return ResponseEntity.noContent().build(); // 204 No Content
    }

    @Operation(
            summary = "Обновить игру",
            description = "Обновляет существующую игру по ID."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Игра обновлена"),
            @ApiResponse(responseCode = "404", description = "Игра не найдена"),
            @ApiResponse(responseCode = "400", description = "Невалидные данные игры")
    })
    @PutMapping("/{id}")
    public ResponseEntity<GameDto> update(@PathVariable String id,
                                          @RequestBody @Valid GameDto dto) {
        GameDto updated = gameService.update(id, dto);
        return ResponseEntity.ok(updated);
    }


}
