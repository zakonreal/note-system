# Система ведения заметок

![Java](https://img.shields.io/badge/Java-17-blue)
![Spring Boot](https://img.shields.io/badge/Spring_Boot-3.2.0-green)
![PostgreSQL](https://img.shields.io/badge/PostgreSQL-13-blue)
![Kafka](https://img.shields.io/badge/Kafka-3.0-orange)

Веб-приложение для управления заметками с возможностью создания, редактирования, поиска заметок, установки напоминаний и экспорта данных.

## Особенности

- 📝 CRUD операции для заметок
- 🔔 Напоминания через Telegram
- 👥 Ролевая модель (USER/ADMIN)
- 📊 Экспорт заметок в Excel
- 🔍 Поиск и фильтрация заметок
- 🖼️ Загрузка изображений к заметкам
- 🐳 Готовые Docker-контейнеры

## Технологический стек

- **Backend**: Java 17, Spring Boot 3.2
- **База данных**: PostgreSQL
- **Очереди сообщений**: Apache Kafka
- **Фронтенд**: FreeMarker, CSS
- **Документация**: Swagger (OpenAPI 3.0)
- **Инфраструктура**: Docker, Docker Compose