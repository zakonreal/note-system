package com.notesystem.controller;

import com.notesystem.model.User;
import com.notesystem.service.ExportService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.io.IOException;

/**
 * Контроллер для экспорта данных.
 * Предоставляет возможность экспорта заметок в Excel.
 */
@Controller
@RequestMapping("/export")
public class ExportController {

    /**
     * Сервис для работы с экспортом данных.
     */
    private final ExportService exportService;

    /**
     * Конструктор класса.
     *
     * @param exportService сервис для экспорта данных
     */
    public ExportController(ExportService exportService) {
        this.exportService = exportService;
    }

    /**
     * Метод для экспорта заметок пользователя в формате Excel.
     * Устанавливает заголовки ответа и отправляет файл на скачивание.
     *
     * @param user           текущий аутентифицированный пользователь
     * @param response       объект ответа HTTP-запроса
     * @throws IOException   если возникает ошибка ввода/вывода при работе с потоком
     */
    @GetMapping("/excel")
    public void exportToExcel(@AuthenticationPrincipal User user,
                              HttpServletResponse response) throws IOException {
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setHeader("Content-Disposition", "attachment; filename=notes.xlsx");
        exportService.exportNotesToExcel(user, response.getOutputStream());
    }
}
