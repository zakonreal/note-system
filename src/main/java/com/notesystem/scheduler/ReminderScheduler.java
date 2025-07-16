package com.notesystem.scheduler;

import com.notesystem.model.Note;
import com.notesystem.service.NoteService;
import com.notesystem.service.KafkaProducerService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import java.time.LocalDateTime;

/**
 * Планировщик для проверки напоминаний.
 * Каждую минуту проверяет активные напоминания и отправляет уведомления.
 */
@Component
public class ReminderScheduler {

    private final NoteService noteService;
    private final KafkaProducerService kafkaProducerService;

    public ReminderScheduler(NoteService noteService,
                             KafkaProducerService kafkaProducerService) {
        this.noteService = noteService;
        this.kafkaProducerService = kafkaProducerService;
    }

    @Scheduled(fixedRate = 60000)
    public void checkReminders() {
        noteService.getPendingReminders().forEach(note -> {
            if (note.getReminder().isBefore(LocalDateTime.now())) {
                String message = String.format("Напоминание: %s (создано %s) просрочено!",
                        note.getTitle(),
                        note.getCreatedDate().toString());
                kafkaProducerService.sendReminder(message);

                // Отмечаем напоминание как обработанное
                note.setReminder(null);
                noteService.updateNote(note.getId(), note, null, null);
            }
        });
    }
}