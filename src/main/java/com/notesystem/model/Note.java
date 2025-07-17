package com.notesystem.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * Модель заметки пользователя.
 * Содержит заголовок, содержание, статус выполнения, дату создания и напоминание.
 */
@Entity
@Table(name = "notes")
@Data
@NoArgsConstructor
public class Note {

    /**
     * Уникальный идентификатор заметки, генерируемый автоматически.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Ссылка на пользователя, которому принадлежит заметка.
     * Отношение "многие к одному", ленивая загрузка.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    /**
     * Заголовок заметки. Не может быть null.
     */
    @Column(nullable = false)
    private String title;

    /**
     * Содержание заметки. Может быть null.
     */
    private String content;

    /**
     * Дата создания заметки. По умолчанию устанавливается в текущую дату.
     */
    @Column(nullable = false)
    private LocalDate createdDate = LocalDate.now();

    /**
     * Флаг, указывающий, выполнена ли заметка. По умолчанию false.
     */
    private boolean completed = false;

    /**
     * Дата и время напоминания о заметке. Может быть null.
     */
    private LocalDateTime reminder;

    /**
     * Путь к изображению, связанному с заметкой. Может быть null.
     */
    private String imagePath;

    /**
     * Конструктор для создания заметки с указанием заголовка и содержания.
     *
     * @param title   заголовок заметки
     * @param content содержание заметки
     */
    public Note(String title, String content) {
        this.title = title;
        this.content = content;
    }
}
