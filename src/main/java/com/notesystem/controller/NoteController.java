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

    @GetMapping("/add")
    public String showAddForm(Model model) {
        model.addAttribute("note", new Note());
        return "add_note";
    }

    @PostMapping("/add")
    public String addNote(
            @AuthenticationPrincipal User user,
            @ModelAttribute Note note,
            @RequestParam(required = false) MultipartFile image,
            @RequestParam(required = false) LocalDateTime reminder) {

        noteService.createNote(note, user, image, reminder);
        return "redirect:/notes";
    }

    @GetMapping("/{id}")
    public String viewNote(@PathVariable Long id,
                           @AuthenticationPrincipal User user,
                           Model model) {
        Note note = noteService.getNoteByIdAndUser(id, user);
        model.addAttribute("note", note);
        return "note";
    }

    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Long id,
                               @AuthenticationPrincipal User user,
                               Model model) {
        Note note = noteService.getNoteByIdAndUser(id, user);
        model.addAttribute("note", note);
        return "edit_note";
    }

    @PostMapping("/edit/{id}")
    public String updateNote(@PathVariable Long id,
                             @ModelAttribute Note note,
                             @RequestParam(required = false) MultipartFile image,
                             @RequestParam(required = false) LocalDateTime reminder) {

        noteService.updateNote(id, note, image, reminder);
        return "redirect:/notes";
    }

    @PostMapping("/delete/{id}")
    public String deleteNote(@PathVariable Long id) {
        noteService.deleteNote(id);
        return "redirect:/notes";
    }

    @PostMapping("/toggle/{id}")
    public String toggleCompletion(@PathVariable Long id) {
        noteService.toggleNoteCompletion(id);
        return "redirect:/notes";
    }
}