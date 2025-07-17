package com.notesystem.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

/**
 * Сервис для отправки уведомлений в Telegram.
 * Обрабатывает сообщения из Kafka и отправляет их в Telegram.
 */
@Service
public class TelegramNotificationService {

    private final RestTemplate restTemplate;
    private final String botToken;
    private final String chatId;

    /**
     * Конструктор класса, инициализирует необходимые параметры.
     *
     * @param botToken токен бота, полученный из конфигурации
     * @param chatId   идентификатор чата, куда будут отправляться сообщения
     */
    public TelegramNotificationService(
            @Value("${notes.telegram.bot-token}") String botToken,
            @Value("${notes.telegram.chat-id}") String chatId
    ) {
        this.restTemplate = new RestTemplate();
        this.botToken = botToken;
        this.chatId = chatId;
    }

    /**
     * Метод, который прослушивает указанную Kafka-тему и отправляет сообщения в Telegram.
     *
     * @param message текст сообщения, полученного из Kafka
     */
    @KafkaListener(
            topics = "${notes.telegram.topic}",
            groupId = "telegram-consumer-group" // Явное указание groupId
    )
    public void sendNotification(String message) {
        String url = String.format(
                "https://api.telegram.org/bot%s/sendMessage?chat_id=%s&text=%s",
                botToken, chatId, message
        );
        restTemplate.getForObject(url, String.class);
    }
}
