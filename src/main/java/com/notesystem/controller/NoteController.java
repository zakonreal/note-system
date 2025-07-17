package com.notesystem.controller;

import com.notesystem.model.Note;
import com.notesystem.model.User;
import com.notesystem.service.NoteService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.time.LocalDateTime;

/**
 * Контроллер для управления заметками пользователей.
 * Обрабатывает CRUD операции и отображение заметок.
 */
@Controller
@RequestMapping("/notes")
public class NoteController {

    private static final int DEFAULT_PAGE_SIZE = 10;
    private final NoteService noteService;

    public NoteController(NoteService noteService) {
        this.noteService = noteService;
    }

    /**
     * Отображает список заметок текущего пользователя с поддержкой пагинации, сортировки и поиска.
     *
     * @param user      Пользователь, для которого загружаются заметки.
     * @param page      Номер страницы (начиная с 0).
     * @param sort      Поле для сортировки (например, createdDate).
     * @param direction Направление сортировки: "asc" или "desc".
     * @param query     Строка запроса для фильтрации заметок.
     * @param model     Модель, передаваемая в представление.
     * @return Имя шаблона (views/notes.html).
     */
    @GetMapping
    public String listNotes(
            @AuthenticationPrincipal User user,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "createdDate") String sort,
            @RequestParam(defaultValue = "desc") String direction,
            @RequestParam(required = false) String query,
            Model model) {

        // Добавим явную проверку на null
        if (user == null) {
            return "redirect:/login";
        }

        Pageable pageable = PageRequest.of(page, DEFAULT_PAGE_SIZE,
                Sort.by(Sort.Direction.fromString(direction), sort));

        Page<Note> notePage = noteService.getUserNotes(user, query, pageable);

        model.addAttribute("notes", notePage.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", notePage.getTotalPages());
        model.addAttribute("sort", sort);
        model.addAttribute("direction", direction);
        model.addAttribute("query", query);
        model.addAttribute("currentUser", user); // Убедимся, что передаем пользователя

        return "notes";
    }

    /**
     * Отображает форму добавления новой заметки.
     *
     * @param model Модель, передаваемая в представление.
     * @return Имя шаблона (views/add_note.html).
     */
    @GetMapping("/add")
    public String showAddForm(Model model) {
        model.addAttribute("note", new Note());
        return "add_note";
    }

    /**
     * Создаёт новую заметку для текущего пользователя.
     *
     * @param user     Текущий аутентифицированный пользователь.
     * @param note     Объект заметки с данными из формы.
     * @param image    Прикреплённый файл-изображение (опционально).
     * @param reminder Время напоминания (опционально).
     * @return Перенаправление на страницу списка заметок.
     */
    @PostMapping("/add")
    public String addNote(
            @AuthenticationPrincipal User user,
            @ModelAttribute Note note,
            @RequestParam(required = false) MultipartFile image,
            @RequestParam(required = false) LocalDateTime reminder) {

        noteService.createNote(note, user, image, reminder);
        return "redirect:/notes";
    }

    /**
     * Отображает конкретную заметку по её идентификатору.
     *
     * @param id   Идентификатор заметки.
     * @param user Текущий пользователь.
     * @param model Модель, передаваемая в представление.
     * @return Имя шаблона (views/note.html).
     */
    @GetMapping("/{id}")
    public String viewNote(@PathVariable Long id,
                           @AuthenticationPrincipal User user,
                           Model model) {
        Note note = noteService.getNoteByIdAndUser(id, user);
        model.addAttribute("note", note);
        return "note";
    }

    /**
     * Отображает форму редактирования заметки.
     *
     * @param id   Идентификатор заметки.
     * @param user Текущий пользователь.
     * @param model Модель, передаваемая в представление.
     * @return Имя шаблона (views/edit_note.html).
     */
    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Long id,
                               @AuthenticationPrincipal User user,
                               Model model) {
        Note note = noteService.getNoteByIdAndUser(id, user);
        model.addAttribute("note", note);
        return "edit_note";
    }

    /**
     * Обновляет существующую заметку.
     *
     * @param id      Идентификатор заметки.
     * @param note    Объект заметки с обновлёнными данными.
     * @param image   Прикреплённый файл-изображение (опционально).
     * @param reminder Время напоминания (опционально).
     * @return Перенаправление на страницу списка заметок.
     */
    @PostMapping("/edit/{id}")
    public String updateNote(@PathVariable Long id,
                             @ModelAttribute Note note,
                             @RequestParam(required = false) MultipartFile image,
                             @RequestParam(required = false) LocalDateTime reminder) {

        noteService.updateNote(id, note, image, reminder);
        return "redirect:/notes";
    }

    /**
     * Удаляет заметку по её идентификатору.
     *
     * @param id Идентификатор заметки.
     * @return Перенаправление на страницу списка заметок.
     */
    @PostMapping("/delete/{id}")
    public String deleteNote(@PathVariable Long id) {
        noteService.deleteNote(id);
        return "redirect:/notes";
    }

    /**
     * Переключает статус выполнения заметки (выполнена / не выполнена).
     *
     * @param id Идентификатор заметки.
     * @return Перенаправление на страницу списка заметок.
     */
    @PostMapping("/toggle/{id}")
    public String toggleCompletion(@PathVariable Long id) {
        noteService.toggleNoteCompletion(id);
        return "redirect:/notes";
    }
}
