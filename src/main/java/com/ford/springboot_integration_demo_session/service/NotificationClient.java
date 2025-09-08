package com.ford.springboot_integration_demo_session.service;

import com.ford.springboot_integration_demo_session.DTO.UserCreatedEvent;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;

@FeignClient(name = "springboot-integration-demo-notification-service", url = "http://localhost:8083")
public interface NotificationClient {

    @PostMapping("/notifications/welcome")
    void sendWelcomeMessage(UserCreatedEvent event);
}
