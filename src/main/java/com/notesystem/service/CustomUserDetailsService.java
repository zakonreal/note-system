package com.notesystem.service;

import com.notesystem.model.User;
import com.notesystem.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import java.util.Optional;

/**
 * Сервис, реализующий интерфейс {@link UserDetailsService}, отвечающий за загрузку информации о пользователе по его логину.
 * <p>
 * Используется Spring Security для аутентификации и авторизации пользователей.
 */
@Service
public class CustomUserDetailsService implements UserDetailsService {

    /**
     * Репозиторий для работы с сущностью {@link User}.
     */
    private final UserRepository userRepository;

    /**
     * Логгер для вывода сообщений в лог-файл.
     */
    private static final Logger log = LoggerFactory.getLogger(CustomUserDetailsService.class);

    /**
     * Конструктор класса, инициализирующий репозиторий пользователя.
     *
     * @param userRepository репозиторий пользователя
     */
    public CustomUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /**
     * Метод загрузки пользователя по его логину.
     * <p>
     * Выполняет поиск пользователя в базе данных. Если пользователь не найден — выбрасывается исключение
     * {@link UsernameNotFoundException}. Если пользователь найден, но неактивен — выбрасывается исключение
     * {@link DisabledException}.
     *
     * @param username логин пользователя
     * @return объект пользователя, реализующий интерфейс {@link UserDetails}
     * @throws UsernameNotFoundException если пользователь с указанным логином не найден
     * @throws DisabledException         если пользователь существует, но неактивен
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        log.info("Trying to load user: {}", username);

        Optional<User> userOpt = userRepository.findByUsername(username);
        if (userOpt.isEmpty()) {
            log.error("User not found: {}", username);
            throw new UsernameNotFoundException("Пользователь не найден: " + username);
        }

        User user = userOpt.get();
        log.info("Found user: {} with password hash: {}", username, user.getPassword());

        if (!user.isActive()) {
            log.warn("User is disabled: {}", username);
            throw new DisabledException("Пользователь деактивирован");
        }

        return user; // Возвращаем кастомного пользователя
    }
}
