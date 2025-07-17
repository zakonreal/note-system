package com.notesystem.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

/**
 * Сервис для хранения загружаемых файлов.
 * Отвечает за сохранение файлов в указанной директории с уникальными именами.
 */
@Service
public class FileStorageService {

    /**
     * Путь к директории, в которой будут храниться загруженные файлы.
     */
    private final Path fileStorageLocation;

    /**
     * Конструктор класса, создающий необходимую директорию при инициализации сервиса.
     *
     * @param uploadDir путь к директории, указанный в конфигурации приложения
     * @throws IOException если возникает ошибка при создании директории
     */
    public FileStorageService(@Value("${notes.storage.upload-dir}") String uploadDir) throws IOException {
        this.fileStorageLocation = Paths.get(uploadDir).toAbsolutePath().normalize();
        Files.createDirectories(this.fileStorageLocation);
    }

    /**
     * Метод для сохранения файла на диск.
     * Генерирует уникальное имя файла, используя UUID. Если оригинальный файл имеет расширение,
     * оно добавляется к имени.
     *
     * @param file загружаемый файл
     * @return имя сохраненного файла
     * @throws IOException если возникает ошибка при чтении или записи файла
     */
    public String store(MultipartFile file) throws IOException {
        String originalName = file.getOriginalFilename();

        // Фикс: обработка файлов без расширения
        String fileName = UUID.randomUUID().toString();
        if (originalName != null && originalName.contains(".")) {
            String extension = originalName.substring(originalName.lastIndexOf("."));
            fileName += extension;
        }

        Path targetLocation = this.fileStorageLocation.resolve(fileName);
        Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);
        return fileName;
    }
}
