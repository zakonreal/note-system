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

    private final ExportService exportService;

    public ExportController(ExportService exportService) {
        this.exportService = exportService;
    }

    @GetMapping("/excel")
    public void exportToExcel(@AuthenticationPrincipal User user,
                              HttpServletResponse response) throws IOException {
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setHeader("Content-Disposition", "attachment; filename=notes.xlsx");
        exportService.exportNotesToExcel(user, response.getOutputStream());
    }
}