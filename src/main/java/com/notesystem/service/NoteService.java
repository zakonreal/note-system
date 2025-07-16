package com.notesystem.service;

import com.notesystem.model.Note;
import com.notesystem.model.User;
import com.notesystem.repository.NoteRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Сервис для управления заметками пользователей.
 * Обеспечивает CRUD операции, обработку изображений и напоминаний.
 */
@Service
public class NoteService {

    private final NoteRepository noteRepository;
    private final FileStorageService fileStorageService;
    private final KafkaProducerService kafkaProducerService;

    public NoteService(NoteRepository noteRepository,
                       FileStorageService fileStorageService,
                       KafkaProducerService kafkaProducerService) {
        this.noteRepository = noteRepository;
        this.fileStorageService = fileStorageService;
        this.kafkaProducerService = kafkaProducerService;
    }

    @Transactional
    public Note createNote(Note note, User user, MultipartFile image, LocalDateTime reminder) {
        note.setUser(user);

        if (image != null && !image.isEmpty()) {
            try {
                String imagePath = fileStorageService.store(image);
                note.setImagePath(imagePath);
            } catch (IOException e) {
                throw new RuntimeException("Ошибка сохранения изображения", e);
            }
        }

        if (reminder != null) {
            note.setReminder(reminder);
            sendReminderNotification(note);
        }

        return noteRepository.save(note);
    }

    private void sendReminderNotification(Note note) {
        String message = String.format("Напоминание: %s в %s",
                note.getTitle(),
                note.getReminder().toString());
        kafkaProducerService.sendReminder(message);
    }

    @Transactional(readOnly = true)
    public Page<Note> getUserNotes(User user, String query, Pageable pageable) {
        if (query == null || query.isEmpty()) {
            return noteRepository.findByUser(user, pageable);
        }
        return noteRepository.searchByUser(user, query, pageable);
    }

    @Transactional
    public Note updateNote(Long id, Note updatedNote, MultipartFile image, LocalDateTime reminder) {
        Note note = noteRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Заметка не найдена"));

        note.setTitle(updatedNote.getTitle());
        note.setContent(updatedNote.getContent());
        note.setCompleted(updatedNote.isCompleted());

        if (image != null && !image.isEmpty()) {
            try {
                String imagePath = fileStorageService.store(image);
                note.setImagePath(imagePath);
            } catch (IOException e) {
                throw new RuntimeException("Ошибка сохранения изображения", e);
            }
        }

        if (reminder != null) {
            note.setReminder(reminder);
            sendReminderNotification(note);
        }

        return noteRepository.save(note);
    }

    @Transactional
    public void deleteNote(Long id) {
        noteRepository.deleteById(id);
    }

    @Transactional
    public void toggleNoteCompletion(Long id) {
        Note note = noteRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Заметка не найдена"));
        note.setCompleted(!note.isCompleted());
        noteRepository.save(note);
    }

    @Transactional(readOnly = true)
    public Note getNoteByIdAndUser(Long id, User user) {
        return noteRepository.findByIdAndUser(id, user)
                .orElseThrow(() -> new IllegalArgumentException("Заметка не найдена или доступ запрещен"));
    }

    @Transactional(readOnly = true)
    public List<Note> getPendingReminders() {
        return noteRepository.findByReminderIsNotNullAndCompletedFalse();
    }
}