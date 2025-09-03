package com.ford.springboot_integration_demo_session.service;

import com.ford.springboot_integration_demo_session.DTO.UserRequestDTO;
import com.ford.springboot_integration_demo_session.DTO.UserResponseDTO;
import com.ford.springboot_integration_demo_session.entity.Users;
import com.ford.springboot_integration_demo_session.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class UserServiceImpl {
    private final UserRepository userRepository;

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public List<UserResponseDTO> getAllUsers() {
        return userRepository.findAll()
                .stream()
                .map(this::mapToResponse)
                .toList(); // Java 16+ has .toList()
    }

    public UserResponseDTO getUserById(Long id) {
        Users user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));
        return mapToResponse(user);
    }

    public UserResponseDTO createUser(UserRequestDTO request) {
        Users user = new Users();
        user.setName(request.name());
        user.setEmail(request.email());
        return mapToResponse(userRepository.save(user));
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
