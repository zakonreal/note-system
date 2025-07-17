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

    /**
     * Путь к директории для хранения загружаемых файлов.
     * Значение задаётся через свойство приложения: 'notes.storage.upload-dir'.
     */
    @Value("${notes.storage.upload-dir}")
    private String uploadDir;

    /**
     * Регистрация обработчиков ресурсов.
     *
     * @param registry Регистратор обработчиков ресурсов, предоставляемый Spring MVC.
     */
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        /**
         * Обработчик для доступа к загруженным изображениям.
         * Все запросы по пути '/images/**' будут отображаться на локальную директорию, указанную в uploadDir.
         */
        registry.addResourceHandler("/images/**")
                .addResourceLocations("file:" + uploadDir + "/");

        /**
         * Обработчик для отображения интерфейса Swagger UI.
         * Все запросы по пути '/swagger-ui/**' будут обслуживаться из ресурсов библиотеки springdoc-openapi-ui.
         */
        registry.addResourceHandler("/swagger-ui/**")
                .addResourceLocations("classpath:/META-INF/resources/webjars/springdoc-openapi-ui/")
                .resourceChain(false);
    }
}