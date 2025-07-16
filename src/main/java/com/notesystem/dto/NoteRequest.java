package com.notesystem.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;
import java.time.LocalDateTime;

/**
 * DTO для запросов на создание/обновление заметок.
 * Содержит валидацию входных данных.
 */
@Data
public class NoteRequest {
    @NotBlank(message = "Заголовок обязателен")
    @Size(max = 255, message = "Заголовок должен быть короче 255 символов")
    private String title;

    @Size(max = 2000, message = "Содержание должно быть короче 2000 символов")
    private String content;

    private boolean completed;

    private LocalDateTime reminder;
}