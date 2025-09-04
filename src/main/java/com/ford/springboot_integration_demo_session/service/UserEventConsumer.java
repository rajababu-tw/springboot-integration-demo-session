package com.ford.springboot_integration_demo_session.service;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class UserEventConsumer {

    @KafkaListener(topics = "user-events", groupId = "user-group")
    public void consume(String message) {
        System.out.println("📩 Received Kafka message: " + message);
    }
}

