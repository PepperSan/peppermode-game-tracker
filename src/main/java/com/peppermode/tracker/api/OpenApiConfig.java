package com.peppermode.tracker.api;

import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.OpenAPI;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI pepperModeGameTrackerApi() {
        return new OpenAPI()
                .info(new Info()
                        .title("PepperMode Game Tracker API")
                        .description("REST API для отслеживания прогресса в видеоиграх")
                        .version("1.0.0")
                );
    }
}

