package com.notesystem.controller;

import com.notesystem.model.User;
import com.notesystem.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * Контроллер, отвечающий за обработку запросов, связанных с аутентификацией и регистрацией пользователей.
 */
@Controller
public class AuthController {

    /**
     * Сервис для работы с пользователями.
     */
    private final UserService userService;

    /**
     * Конструктор класса.
     *
     * @param userService сервис для работы с пользователями
     */
    public AuthController(UserService userService) {
        this.userService = userService;
    }

    /**
     * Обрабатывает GET-запросы на страницу входа.
     * Добавляет сообщения об ошибках в модель, если они переданы как параметры запроса.
     *
     * @param error      параметр ошибки аутентификации
     * @param disabled   параметр деактивации пользователя
     * @param model      модель для передачи данных в шаблон
     * @return имя шаблона "login"
     */
    @GetMapping("/login")
    public String showLoginForm(
            @RequestParam(value = "error", required = false) String error,
            @RequestParam(value = "disabled", required = false) String disabled,
            Model model) {

        if (error != null) {
            model.addAttribute("error", "Неверное имя пользователя или пароль");
        }
        if (disabled != null) {
            model.addAttribute("error", "Пользователь деактивирован");
        }
        return "login";
    }

    /**
     * Обрабатывает GET-запросы на страницу регистрации.
     * Добавляет новый объект User в модель для отображения формы.
     *
     * @param model модель для передачи данных в шаблон
     * @return имя шаблона "register"
     */
    @GetMapping("/register")
    public String showRegistrationForm(Model model) {
        model.addAttribute("user", new User()); // Фикс: добавление объекта пользователя
        return "register";
    }

    /**
     * Обрабатывает POST-запросы на регистрацию нового пользователя.
     * Проверяет совпадение паролей и регистрирует пользователя через сервис.
     *
     * @param username       логин пользователя
     * @param password       пароль пользователя
     * @param confirmPassword подтверждённый пароль
     * @param model          модель для передачи данных в шаблон
     * @return редирект на "/login" или "register" при ошибке
     */
    @PostMapping("/register")
    public String registerUser(@RequestParam String username,
                               @RequestParam String password,
                               @RequestParam String confirmPassword,
                               Model model) {
        try {
            if (!password.equals(confirmPassword)) {
                throw new IllegalArgumentException("Пароли не совпадают");
            }

            userService.registerUser(username, password);
            return "redirect:/login";
        } catch (IllegalArgumentException e) {
            model.addAttribute("error", e.getMessage());
            model.addAttribute("user", new User()); // Фикс: добавление объекта
            return "register";
        }
    }
}
