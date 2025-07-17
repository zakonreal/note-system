package com.notesystem.repository;

import com.notesystem.model.Note;
import com.notesystem.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

/**
 * Репозиторий для работы с сущностью Note.
 * Предоставляет методы для поиска, фильтрации и управления заметками пользователей.
 */
@Repository
public interface NoteRepository extends JpaRepository<Note, Long> {

    /**
     * Возвращает страницу заметок, принадлежащих указанному пользователю.
     *
     * @param user    пользователь, чьи заметки необходимо получить
     * @param pageable информация о пагинации
     * @return страница объектов Note, связанных с пользователем
     */
    Page<Note> findByUser(User user, Pageable pageable);

    /**
     * Выполняет поиск заметок по пользователю и текстовому запросу в заголовке или содержании.
     * Поиск нечувствителен к регистру.
     *
     * @param user  пользователь, чьи заметки необходимо найти
     * @param query строка поиска (часть заголовка или содержания)
     * @param pageable информация о пагинации
     * @return страница найденных заметок
     */
    @Query("SELECT n FROM Note n WHERE n.user = :user AND " +
            "(LOWER(n.title) LIKE LOWER(CONCAT('%', :query, '%')) OR " +
            "LOWER(n.content) LIKE LOWER(CONCAT('%', :query, '%')))")
    Page<Note> searchByUser(User user, String query, Pageable pageable);

    /**
     * Возвращает список всех заметок, принадлежащих указанному пользователю.
     *
     * @param user пользователь, чьи заметки необходимо получить
     * @return список объектов Note
     */
    List<Note> findByUser(User user);

    /**
     * Возвращает список всех активных заметок с напоминанием, которые ещё не выполнены.
     *
     * @return список объектов Note с ненулевым напоминанием и флагом completed = false
     */
    List<Note> findByReminderIsNotNullAndCompletedFalse();

    /**
     * Находит опциональный объект Note по его идентификатору и владельцу.
     *
     * @param id   идентификатор заметки
     * @param user пользователь, который должен быть владельцем заметки
     * @return Optional<Note> объект, если заметка найдена, иначе пустой Optional
     */
    Optional<Note> findByIdAndUser(Long id, User user);
}
