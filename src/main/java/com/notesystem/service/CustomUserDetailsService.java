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

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;
    private static final Logger log = LoggerFactory.getLogger(CustomUserDetailsService.class);

    public CustomUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

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