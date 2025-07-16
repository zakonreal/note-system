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

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(nullable = false)
    private String title;

    private String content;

    @Column(nullable = false)
    private LocalDate createdDate = LocalDate.now();

    private boolean completed = false;

    private LocalDateTime reminder;

    private String imagePath;

    public Note(String title, String content) {
        this.title = title;
        this.content = content;
    }
}