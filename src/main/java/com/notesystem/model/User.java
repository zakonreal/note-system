package com.notesystem.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * Сущность, представляющая пользователя системы.
 * Реализует интерфейс {@link UserDetails} для интеграции с Spring Security.
 */
@Entity
@Table(name = "users")
@Data
@NoArgsConstructor
public class User implements UserDetails {

    /**
     * Уникальный идентификатор пользователя, генерируемый автоматически.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Имя пользователя. Обязательное поле, должно быть уникальным.
     */
    @Column(nullable = false, unique = true)
    private String username;

    /**
     * Пароль пользователя. Обязательное поле.
     */
    @Column(nullable = false)
    private String password;

    /**
     * Флаг активности пользователя. По умолчанию установлен в true.
     */
    @Column(nullable = false)
    private boolean active = true;

    /**
     * Роль пользователя в системе. Может принимать значения из перечисления {@link Role}.
     * По умолчанию роль пользователя — USER.
     */
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role = Role.USER;

    /**
     * Список заметок, принадлежащих пользователю.
     * Определяет связь "один-ко-многим" с сущностью {@link Note}.
     */
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Note> notes = new ArrayList<>();

    /**
     * Перечисление ролей, доступных в системе.
     */
    public enum Role {
        /**
         * Пользователь с обычными правами.
         */
        USER,

        /**
         * Администратор с расширенными правами.
         */
        ADMIN
    }

    /**
     * Конструктор, создающий нового пользователя.
     *
     * @param username имя пользователя
     * @param password пароль пользователя
     */
    public User(String username, String password) {
        this.username = username;
        this.password = password;
    }

    /**
     * Возвращает список ролей пользователя в формате {@link GrantedAuthority},
     * необходимом для работы Spring Security.
     *
     * @return коллекция ролей пользователя
     */
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.singleton(new SimpleGrantedAuthority("ROLE_" + role.name()));
    }

    /**
     * Проверяет, не истёк ли срок действия учётной записи.
     * В данной реализации всегда возвращает true.
     *
     * @return true, если срок действия учётной записи не истёк
     */
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    /**
     * Проверяет, не заблокирована ли учётная запись.
     * В данной реализации всегда возвращает true.
     *
     * @return true, если учётная запись не заблокирована
     */
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    /**
     * Проверяет, не истёк ли срок действия учетных данных (пароля).
     * В данной реализации всегда возвращает true.
     *
     * @return true, если срок действия учетных данных не истёк
     */
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    /**
     * Проверяет, активна ли учётная запись.
     *
     * @return true, если учётная запись активна
     */
    @Override
    public boolean isEnabled() {
        return active;
    }
}
