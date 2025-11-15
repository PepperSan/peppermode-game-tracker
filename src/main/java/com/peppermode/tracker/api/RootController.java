package com.peppermode.tracker.api;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
public class RootController {

    @GetMapping("/")
    public Map<String, Object> root() {
        return Map.of(
                "name", "PepperMode Game Tracker API",
                "version", "1.0-SNAPSHOT",
                "endpoints", List.of(
                        "/api/games",
                        "/api/sessions",
                        "/api/ping"
                )
        );
    }
}

