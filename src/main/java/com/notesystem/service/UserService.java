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

    @Transactional
    public void toggleUserStatus(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("Пользователь не найден"));
        user.setActive(!user.isActive());
        userRepository.save(user);
    }

    @Transactional
    public void changeUserRole(Long userId, User.Role role) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("Пользователь не найден"));
        user.setRole(role);
        userRepository.save(user);
    }

    @Transactional(readOnly = true)
    public Page<User> getAllUsers(String query, Pageable pageable) {
        if (query == null || query.isEmpty()) {
            return userRepository.findAll(pageable);
        }
        return userRepository.findByUsernameContainingIgnoreCase(query, pageable);
    }

    @Transactional(readOnly = true)
    public Optional<User> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }
}