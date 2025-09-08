package com.ford.springboot_integration_demo_session.service;

import com.ford.springboot_integration_demo_session.DTO.UserCreatedEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class UserEventConsumer {

    @KafkaListener(topics = "user-events", groupId = "user-group")
    public void consume(UserCreatedEvent event) {
        log.info("Consumed event Aysnchronously: {}", event);
    }

}


