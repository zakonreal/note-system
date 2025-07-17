package com.notesystem.controller;

import com.notesystem.model.User;
import com.notesystem.service.UserService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

/**
 * Контроллер для административных функций.
 * Доступен только пользователям с ролью ADMIN.
 */
@Controller
@RequestMapping("/admin")
@PreAuthorize("hasRole('ADMIN')")
public class AdminController {

    private static final int DEFAULT_PAGE_SIZE = 10;
    private final UserService userService;

    public AdminController(UserService userService) {
        this.userService = userService;
    }

    /**
     * Отображает список пользователей с возможностью поиска и пагинации.
     *
     * @param page Номер страницы (по умолчанию 0)
     * @param query Строка запроса для фильтрации пользователей (необязательно)
     * @param model Модель, передаваемая в представление
     * @return Имя шаблона представления "admin/users"
     */
    @GetMapping
    public String listUsers(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(required = false) String query,
            Model model) {

        Pageable pageable = PageRequest.of(page, DEFAULT_PAGE_SIZE);
        Page<User> userPage = userService.getAllUsers(query, pageable);

        model.addAttribute("users", userPage.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", userPage.getTotalPages());
        model.addAttribute("query", query);

        return "admin/users";
    }

    /**
     * Переключает статус пользователя (активен/заблокирован).
     *
     * @param id Идентификатор пользователя
     * @return Перенаправление на страницу администратора
     */
    @PostMapping("/toggle/{id}")
    public String toggleUserStatus(@PathVariable Long id) {
        userService.toggleUserStatus(id);
        return "redirect:/admin";
    }

    /**
     * Изменяет роль пользователя.
     *
     * @param id Идентификатор пользователя
     * @param role Новая роль пользователя
     * @return Перенаправление на страницу администратора
     */
    @PostMapping("/role/{id}")
    public String changeUserRole(@PathVariable Long id, @RequestParam User.Role role) {
        userService.changeUserRole(id, role);
        return "redirect:/admin";
    }
}
