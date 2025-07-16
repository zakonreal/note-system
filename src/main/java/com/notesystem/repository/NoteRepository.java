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

@Repository
public interface NoteRepository extends JpaRepository<Note, Long> {
    Page<Note> findByUser(User user, Pageable pageable);

    // Исправленный запрос
    @Query("SELECT n FROM Note n WHERE n.user = :user AND " +
            "(LOWER(n.title) LIKE LOWER(CONCAT('%', :query, '%')) OR " +
            "LOWER(n.content) LIKE LOWER(CONCAT('%', :query, '%')))")
    Page<Note> searchByUser(User user, String query, Pageable pageable);

    List<Note> findByUser(User user);
    List<Note> findByReminderIsNotNullAndCompletedFalse();
    Optional<Note> findByIdAndUser(Long id, User user);
}