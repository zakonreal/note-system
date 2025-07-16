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

    private final KafkaTemplate<String, String> kafkaTemplate;
    private final String topic;

    public KafkaProducerService(KafkaTemplate<String, String> kafkaTemplate,
                                @Value("${notes.telegram.topic}") String topic) {
        this.kafkaTemplate = kafkaTemplate;
        this.topic = topic;
    }

    public void sendReminder(String message) {
        kafkaTemplate.send(topic, message);
    }
}