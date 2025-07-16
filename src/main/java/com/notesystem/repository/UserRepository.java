package com.notesystem.repository;

import com.notesystem.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

/**
 * Репозиторий для работы с пользователями.
 * Поддерживает поиск по имени пользователя и пагинацию.
 */
@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);
    boolean existsByUsername(String username);
    Page<User> findByUsernameContainingIgnoreCase(String username, Pageable pageable);
    Page<User> findAll(Pageable pageable);
}