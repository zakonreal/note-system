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
    public static void main(String[] args) {
        SpringApplication.run(NotesystemApplication.class, args);
    }
}