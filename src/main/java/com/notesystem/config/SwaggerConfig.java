package com.notesystem.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import org.springframework.context.annotation.Configuration;

/**
 * Конфигурация Swagger/OpenAPI для документации API.
 */
@Configuration
@OpenAPIDefinition(
        info = @Info(
                title = "API Системы Заметок",
                version = "1.0",
                description = "Документация REST API для системы управления заметками"
        )
)
@SecurityScheme(
        name = "bearerAuth",
        type = SecuritySchemeType.HTTP,
        scheme = "bearer",
        bearerFormat = "JWT"
)
/**
 * Класс SwaggerConfig настраивает документацию OpenAPI для приложения Spring.
 * <p>
 * Использует аннотации из библиотеки Swagger, чтобы задать метаданные API:
 * - заголовок, версию и описание API
 * - схему безопасности Bearer (JWT)
 */
public class SwaggerConfig {
}