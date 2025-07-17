package com.notesystem.service;

import com.notesystem.model.Note;
import com.notesystem.model.User;
import com.notesystem.repository.NoteRepository;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;
import java.io.IOException;
import java.io.OutputStream;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * Сервис для экспорта заметок в формат Excel.
 * Генерирует XLSX-файл со списком заметок пользователя.
 */
@Service
public class ExportService {

    private final NoteRepository noteRepository;
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private static final DateTimeFormatter DATETIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    /**
     * Конструктор класса, инициализирующий репозиторий заметок.
     *
     * @param noteRepository Репозиторий для работы с заметками
     */
    public ExportService(NoteRepository noteRepository) {
        this.noteRepository = noteRepository;
    }

    /**
     * Экспортирует список заметок пользователя в Excel-файл.
     *
     * @param user           Пользователь, чьи заметки необходимо экспортировать
     * @param outputStream   Поток вывода, в который будет записан Excel-файл
     * @throws IOException   В случае ошибки ввода/вывода
     */
    public void exportNotesToExcel(User user, OutputStream outputStream) throws IOException {
        List<Note> notes = noteRepository.findByUser(user);

        try (Workbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet("Notes");

            // Стиль для заголовков
            CellStyle headerStyle = workbook.createCellStyle();
            Font headerFont = workbook.createFont();
            headerFont.setBold(true);
            headerStyle.setFont(headerFont);

            // Заголовки столбцов
            Row headerRow = sheet.createRow(0);
            String[] columns = {"ID", "Заголовок", "Содержание", "Дата создания", "Статус", "Напоминание", "Изображение"};

            for (int i = 0; i < columns.length; i++) {
                Cell cell = headerRow.createCell(i);
                cell.setCellValue(columns[i]);
                cell.setCellStyle(headerStyle);
            }

            // Заполнение данными
            int rowNum = 1;
            for (Note note : notes) {
                Row row = sheet.createRow(rowNum++);

                row.createCell(0).setCellValue(note.getId());
                row.createCell(1).setCellValue(note.getTitle());
                row.createCell(2).setCellValue(note.getContent());
                row.createCell(3).setCellValue(note.getCreatedDate().format(DATE_FORMATTER));
                row.createCell(4).setCellValue(note.isCompleted() ? "Завершено" : "В процессе");

                if (note.getReminder() != null) {
                    row.createCell(5).setCellValue(note.getReminder().format(DATETIME_FORMATTER));
                } else {
                    row.createCell(5).setCellValue("");
                }

                row.createCell(6).setCellValue(note.getImagePath() != null ? note.getImagePath() : "");
            }

            // Автоматическое изменение ширины столбцов
            for (int i = 0; i < columns.length; i++) {
                sheet.autoSizeColumn(i);
            }

            workbook.write(outputStream);
        }
    }
}
