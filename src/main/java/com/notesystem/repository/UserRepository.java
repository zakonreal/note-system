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

    /**
     * Ищет пользователя по имени.
     *
     * @param username имя пользователя
     * @return опциональный объект пользователя, если он найден
     */
    Optional<User> findByUsername(String username);

    /**
     * Проверяет, существует ли пользователь с заданным именем.
     *
     * @param username имя пользователя
     * @return true, если пользователь существует, иначе false
     */
    boolean existsByUsername(String username);

    /**
     * Ищет пользователей по части имени (без учета регистра) с поддержкой пагинации.
     *
     * @param username часть имени пользователя
     * @param pageable информация о пагинации
     * @return страница пользователей, соответствующих критерию
     */
    Page<User> findByUsernameContainingIgnoreCase(String username, Pageable pageable);

    /**
     * Получает всех пользователей с поддержкой пагинации.
     *
     * @param pageable информация о пагинации
     * @return страница пользователей
     */
    Page<User> findAll(Pageable pageable);
}
