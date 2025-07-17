package com.notesystem;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * Главный класс приложения.
 * Запускает Spring Boot приложение и включает планировщик задач.
 */
@SpringBootApplication
@EnableScheduling
public class NotesystemApplication {
    /**
     * Точка входа в приложение.
     * Запускает Spring Boot-приложение с передачей аргументов командной строки.
     *
     * @param args аргументы командной строки, переданные при запуске приложения
     */
    public static void main(String[] args) {
        SpringApplication.run(NotesystemApplication.class, args);
    }
}
