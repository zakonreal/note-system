package com.notesystem.service;

import com.notesystem.model.User;
import com.notesystem.repository.UserRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.Optional;

/**
 * Сервис для управления пользователями.
 * Обеспечивает регистрацию, изменение статуса и роли пользователей.
 */
@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;

        // Для отладки: выводим хеш пароля "admin"
        System.out.println("DEBUG: Hash for 'admin': " + passwordEncoder.encode("admin"));
    }

    /**
     * Регистрация нового пользователя.
     * Хеширует пароль перед сохранением в репозиторий.
     *
     * @param username имя пользователя
     * @param password пароль пользователя
     * @return зарегистрированный пользователь
     * @throws IllegalArgumentException если имя пользователя уже занято
     */
    @Transactional
    public User registerUser(String username, String password) {
        if (userRepository.existsByUsername(username)) {
            throw new IllegalArgumentException("Имя пользователя уже занято");
        }

        // Убедимся, что пароль хешируется
        String encodedPassword = passwordEncoder.encode(password);
        System.out.println("DEBUG: Registering user: " + username + " with password hash: " + encodedPassword);

        User user = new User(username, encodedPassword);
        return userRepository.save(user);
    }

    /**
     * Переключает статус пользователя между активным и неактивным.
     *
     * @param userId идентификатор пользователя
     * @throws IllegalArgumentException если пользователь не найден
     */
    @Transactional
    public void toggleUserStatus(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("Пользователь не найден"));
        user.setActive(!user.isActive());
        userRepository.save(user);
    }

    /**
     * Изменяет роль пользователя.
     *
     * @param userId идентификатор пользователя
     * @param role новая роль пользователя
     * @throws IllegalArgumentException если пользователь не найден
     */
    @Transactional
    public void changeUserRole(Long userId, User.Role role) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("Пользователь не найден"));
        user.setRole(role);
        userRepository.save(user);
    }

    /**
     * Получает список пользователей с возможностью фильтрации по имени и пагинации.
     *
     * @param query  строка для поиска по имени (опционально)
     * @param pageable параметры пагинации
     * @return страница пользователей
     */
    @Transactional(readOnly = true)
    public Page<User> getAllUsers(String query, Pageable pageable) {
        if (query == null || query.isEmpty()) {
            return userRepository.findAll(pageable);
        }
        return userRepository.findByUsernameContainingIgnoreCase(query, pageable);
    }

    /**
     * Находит пользователя по его имени.
     *
     * @param username имя пользователя
     * @return опциональный объект пользователя
     */
    @Transactional(readOnly = true)
    public Optional<User> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }
}
