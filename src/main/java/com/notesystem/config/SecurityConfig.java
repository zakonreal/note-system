package com.notesystem.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

/**
 * Конфигурационный класс, который настраивает безопасность приложения Spring Security.
 * Определяет правила авторизации, аутентификации, маршруты для входа/выхода и др.
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    /**
     * Сервис для получения деталей пользователя из системы.
     */
    private final UserDetailsService userDetailsService;

    /**
     * Логгер для вывода сообщений о безопасности.
     */
    private static final Logger log = LoggerFactory.getLogger(SecurityConfig.class);

    /**
     * Конструктор класса, инициализирующий сервис пользователей.
     *
     * @param userDetailsService сервис для получения информации о пользователях
     */
    public SecurityConfig(UserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    /**
     * Метод, создающий цепочку фильтров безопасности.
     *
     * @param http объект HttpSecurity, используемый для конфигурации прав доступа и других параметров безопасности
     * @return настроенная цепочка фильтров безопасности
     * @throws Exception может быть выброшено исключение при настройке
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers("/", "/register", "/login", "/css/**", "/swagger-ui/**", "/v3/api-docs/**").permitAll()
                        .requestMatchers("/admin/**").hasRole("ADMIN")
                        .anyRequest().authenticated()
                )
                .formLogin(form -> form
                        .loginPage("/login")
                        .defaultSuccessUrl("/notes", true)
                        .failureHandler((request, response, exception) -> {
                            log.error("Login failed: {}", exception.getMessage());
                            if (exception instanceof DisabledException) {
                                response.sendRedirect("/login?disabled=true");
                            } else {
                                response.sendRedirect("/login?error=true");
                            }
                        })
                        .permitAll()
                )
                .logout(logout -> logout
                        .logoutUrl("/logout")
                        .logoutSuccessUrl("/")
                        .permitAll()
                )
                .authenticationProvider(authenticationProvider())
                .csrf(AbstractHttpConfigurer::disable);

        return http.build();
    }

    /**
     * Метод, создающий экземпляр кодировщика паролей BCrypt.
     *
     * @return объект PasswordEncoder, реализующий алгоритм хеширования BCrypt
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * Метод, создающий провайдера аутентификации на основе DAO.
     * Используется для проверки учетных данных пользователя.
     *
     * @return настроенный провайдер аутентификации
     */
    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        log.info("Creating DaoAuthenticationProvider");
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }
}
