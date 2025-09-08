package com.ford.springboot_integration_demo_session.subscriber;

import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class RedisMessageSubscriber implements MessageListener {

    @Override
    public void onMessage(Message message, byte[] pattern) {
        log.info("Received Message from topic {} and message is {}",new String(pattern),new String(message.getBody()));
        //System.out.println("Received Message: " + new String(message.getBody()));
    }
}
