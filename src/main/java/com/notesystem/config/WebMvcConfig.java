package com.notesystem.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Конфигурация веб-слоя приложения.
 * Настраивает обработчики ресурсов и статических файлов.
 */
@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    @Value("${notes.storage.upload-dir}")
    private String uploadDir;

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // Доступ к загруженным изображениям
        registry.addResourceHandler("/images/**")
                .addResourceLocations("file:" + uploadDir + "/");

        // Документация Swagger
        registry.addResourceHandler("/swagger-ui/**")
                .addResourceLocations("classpath:/META-INF/resources/webjars/springdoc-openapi-ui/")
                .resourceChain(false);
    }
}