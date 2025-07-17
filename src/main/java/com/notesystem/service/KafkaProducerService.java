package com.notesystem.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

/**
 * Сервис для отправки сообщений в Kafka.
 * Используется для отправки уведомлений о напоминаниях.
 */
@Service
public class KafkaProducerService {

    /**
     * Шаблон Kafka, используемый для отправки сообщений.
     */
    private final KafkaTemplate<String, String> kafkaTemplate;

    /**
     * Тема Kafka, в которую будут отправляться сообщения.
     * Значение загружается из конфигурации Spring: ${notes.telegram.topic}.
     */
    private final String topic;

    /**
     * Конструктор класса KafkaProducerService.
     *
     * @param kafkaTemplate шаблон Kafka для отправки сообщений
     * @param topic         тема Kafka, в которую отправляются сообщения
     */
    public KafkaProducerService(KafkaTemplate<String, String> kafkaTemplate,
                                @Value("${notes.telegram.topic}") String topic) {
        this.kafkaTemplate = kafkaTemplate;
        this.topic = topic;
    }

    /**
     * Метод для отправки сообщения с напоминанием в указанную тему Kafka.
     *
     * @param message текст сообщения, которое будет отправлено
     */
    public void sendReminder(String message) {
        kafkaTemplate.send(topic, message);
    }
}
