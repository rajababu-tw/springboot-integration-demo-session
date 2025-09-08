package com.ford.springboot_integration_demo_session.service;

import com.ford.springboot_integration_demo_session.DTO.UserCreatedEvent;
import com.ford.springboot_integration_demo_session.DTO.UserRequestDTO;
import com.ford.springboot_integration_demo_session.DTO.UserResponseDTO;
import com.ford.springboot_integration_demo_session.entity.Users;
import com.ford.springboot_integration_demo_session.publisher.RedisMessagePublisher;
import com.ford.springboot_integration_demo_session.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.List;

@Service
@Slf4j
public class UserServiceImpl {
    private final UserRepository userRepository;
    private final RedisTemplate redisTemplate;
    private final RedisMessagePublisher redisMessagePublisher;
    private static final String USER_HASH_KEY = "USER";
    private final UserEventProducer userEventProducer;
    private final NotificationClient notificationClient;

    public UserServiceImpl(UserRepository userRepository, RedisTemplate redisTemplate, RedisMessagePublisher redisMessagePublisher, UserEventProducer userEventProducer,NotificationClient notificationClient) {
        this.userRepository = userRepository;
        this.redisTemplate = redisTemplate;
        this.redisMessagePublisher = redisMessagePublisher;
        this.userEventProducer = userEventProducer;
        this.notificationClient = notificationClient;
    }

    public UserResponseDTO createUser(UserRequestDTO request) {
        Users user = Users.builder()
                .name(request.name())
                .email(request.email())
                .build();

        Users savedUser = userRepository.save(user);
        UserResponseDTO responseDTO=mapToResponse(savedUser);
        UserCreatedEvent userCreatedEvent=new UserCreatedEvent(responseDTO.id(),responseDTO.name(),responseDTO.email());


        // Publish event to Kafka - Event Driven Asynchronous Call
        userEventProducer.sendUserCreatedEvent(userCreatedEvent);

        //Synchronous Call - Feign Client
        notificationClient.sendWelcomeMessage(userCreatedEvent);

        //Redis
        redisTemplate.opsForValue().set(USER_HASH_KEY + ":" + responseDTO.id(), responseDTO, Duration.ofMinutes(2));
        redisMessagePublisher.publish(responseDTO.toString());

        return responseDTO;
    }

    public List<UserResponseDTO> getAllUsers() {
        return userRepository.findAll()
                .stream()
                .map(this::mapToResponse)
                .toList(); // Java 16+ has .toList()
    }

    public UserResponseDTO getUserById(Long id) {
        //Object cachedUser = redisTemplate.opsForHash().get(USER_HASH_KEY, id.toString());
        Object cachedUser = redisTemplate.opsForValue().get(USER_HASH_KEY + ":" + id);
        if (cachedUser != null) {
            log.info("Data return from cache for user id {}", id);
            return (UserResponseDTO) cachedUser;
        }
        log.warn("Cache miss for user id={}, querying DB", id);
        Users user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));
        UserResponseDTO response = mapToResponse(user);
//        redisTemplate.opsForHash().put(USER_HASH_KEY, id.toString(), response);
        redisTemplate.opsForValue().set(USER_HASH_KEY + ":" + id, response, Duration.ofMinutes(1));
        return response;
    }


    public UserResponseDTO updateUser(Long id, UserRequestDTO request) {
        Users user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));
        user.setName(request.name());
        user.setEmail(request.email());
        return mapToResponse(userRepository.save(user));
    }

    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }

    private UserResponseDTO mapToResponse(Users user) {
        return new UserResponseDTO(user.getId(), user.getName(), user.getEmail());
    }
}
