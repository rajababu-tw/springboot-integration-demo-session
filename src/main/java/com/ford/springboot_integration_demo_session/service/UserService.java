package com.ford.springboot_integration_demo_session.service;

import com.ford.springboot_integration_demo_session.entity.Users;
import com.ford.springboot_integration_demo_session.repository.UserRepository;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    // Caches the result of this method
    @Cacheable(value = "users", key = "#id")
    public Users getUserById(Long id) {
        System.out.println("Fetching from DB for id: " + id);
        return userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }

    // Updates cache when a user is updated
    @CachePut(value = "users", key = "#user.id")
    public Users updateUser(Users user) {
        System.out.println("Updating DB + Cache for id: " + user.getId());
        return userRepository.save(user);
    }

    // Removes from cache when a user is deleted
    @CacheEvict(value = "users", key = "#id")
    public void deleteUser(Long id) {
        System.out.println("Deleting user from DB + Cache for id: " + id);
        userRepository.deleteById(id);
    }
}
