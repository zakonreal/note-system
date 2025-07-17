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
    /**
     * Заголовок заметки.
     * <p>
     * Обязательное поле. Максимальная длина — 255 символов.
     */
    @NotBlank(message = "Заголовок обязателен")
    @Size(max = 255, message = "Заголовок должен быть короче 255 символов")
    private String title;

    /**
     * Содержание заметки.
     * <p>
     * Необязательное поле. Максимальная длина — 2000 символов.
     */
    @Size(max = 2000, message = "Содержание должно быть короче 2000 символов")
    private String content;

    /**
     * Флаг завершения заметки.
     * <p>
     * Используется для отметки о выполнении задачи.
     */
    private boolean completed;

    /**
     * Дата и время напоминания.
     * <p>
     * Может быть использовано для организации уведомлений.
     */
    private LocalDateTime reminder;
}
