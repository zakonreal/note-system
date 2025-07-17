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

    /**
     * Создаёт новую заметку для указанного пользователя.
     *
     * @param note    Объект заметки с данными
     * @param user    Пользователь, которому принадлежит заметка
     * @param image   Прикреплённое изображение (может быть null)
     * @param reminder Время напоминания (может быть null)
     * @return Сохранённая заметка
     * @throws RuntimeException при ошибке сохранения изображения
     */
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

    /**
     * Отправляет уведомление о наступлении напоминания.
     *
     * @param note Заметка, для которой нужно отправить напоминание
     */
    private void sendReminderNotification(Note note) {
        String message = String.format("Напоминание: %s в %s",
                note.getTitle(),
                note.getReminder().toString());
        kafkaProducerService.sendReminder(message);
    }

    /**
     * Получает список заметок текущего пользователя с возможностью фильтрации и пагинации.
     *
     * @param user    Пользователь, чьи заметки необходимо получить
     * @param query   Строка для поиска (опционально)
     * @param pageable Объект пагинации
     * @return Страница с результатами
     */
    @Transactional(readOnly = true)
    public Page<Note> getUserNotes(User user, String query, Pageable pageable) {
        if (query == null || query.isEmpty()) {
            return noteRepository.findByUser(user, pageable);
        }
        return noteRepository.searchByUser(user, query, pageable);
    }

    /**
     * Обновляет существующую заметку.
     *
     * @param id           Идентификатор заметки
     * @param updatedNote  Объект заметки с обновлёнными данными
     * @param image        Новое изображение (может быть null)
     * @param reminder     Новое время напоминания (может быть null)
     * @return Обновлённая заметка
     * @throws IllegalArgumentException если заметка не найдена
     * @throws RuntimeException         при ошибке сохранения изображения
     */
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

    /**
     * Удаляет заметку по её идентификатору.
     *
     * @param id Идентификатор заметки
     */
    @Transactional
    public void deleteNote(Long id) {
        noteRepository.deleteById(id);
    }

    /**
     * Переключает статус завершения заметки.
     *
     * @param id Идентификатор заметки
     * @return Обновлённая заметка
     * @throws IllegalArgumentException если заметка не найдена
     */
    @Transactional
    public void toggleNoteCompletion(Long id) {
        Note note = noteRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Заметка не найдена"));
        note.setCompleted(!note.isCompleted());
        noteRepository.save(note);
    }

    /**
     * Получает заметку по её идентификатору и проверяет, что она принадлежит указанному пользователю.
     *
     * @param id   Идентификатор заметки
     * @param user Пользователь, который запрашивает заметку
     * @return Заметка, если доступ разрешён
     * @throws IllegalArgumentException если заметка не найдена или доступ запрещён
     */
    @Transactional(readOnly = true)
    public Note getNoteByIdAndUser(Long id, User user) {
        return noteRepository.findByIdAndUser(id, user)
                .orElseThrow(() -> new IllegalArgumentException("Заметка не найдена или доступ запрещен"));
    }

    /**
     * Получает список всех активных напоминаний (незавершённых заметок).
     *
     * @return Список заметок с непустыми напоминаниями
     */
    @Transactional(readOnly = true)
    public List<Note> getPendingReminders() {
        return noteRepository.findByReminderIsNotNullAndCompletedFalse();
    }
}
