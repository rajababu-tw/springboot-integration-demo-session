package com.ford.springboot_integration_demo_session.service;

import com.ford.springboot_integration_demo_session.DTO.UserRequestDTO;
import com.ford.springboot_integration_demo_session.DTO.UserResponseDTO;
import com.ford.springboot_integration_demo_session.entity.Users;
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
    private static final String USER_HASH_KEY = "USER";

    public UserServiceImpl(UserRepository userRepository, RedisTemplate redisTemplate) {
        this.userRepository = userRepository;
        this.redisTemplate = redisTemplate;
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

    public UserResponseDTO createUser(UserRequestDTO request) {
        Users user = new Users();
        user.setName(request.name());
        user.setEmail(request.email());
        UserResponseDTO response = mapToResponse(userRepository.save(user));
        //Save to redis
        //redisTemplate.opsForHash().put(USER_HASH_KEY,user.getId().toString(),response);
        redisTemplate.opsForValue().set(USER_HASH_KEY + ":" + response.id(), response, Duration.ofMinutes(1));
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
