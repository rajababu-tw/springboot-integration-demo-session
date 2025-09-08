package com.ford.springboot_integration_demo_session.controller;

import com.ford.springboot_integration_demo_session.DTO.UserRequestDTO;
import com.ford.springboot_integration_demo_session.DTO.UserResponseDTO;
import com.ford.springboot_integration_demo_session.entity.Users;
import com.ford.springboot_integration_demo_session.service.UserService;
import com.ford.springboot_integration_demo_session.service.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {
    private final UserServiceImpl userService;
    private final UserService userServiceImplWithAnnotations;
    @Autowired
    public UserController(UserServiceImpl userServiceImpl, UserService userService, UserServiceImpl userService1, UserService userServiceImplWithAnnotations) {
        this.userService = userService1;
        this.userServiceImplWithAnnotations = userServiceImplWithAnnotations;
    }

    public UserController(UserServiceImpl userService, UserService userServiceImplWithAnnotations) {
        this.userService = userService;
        this.userServiceImplWithAnnotations = userServiceImplWithAnnotations;
    }

    @GetMapping
    public List<UserResponseDTO> getAllUsers() {
        return userService.getAllUsers();
    }

    @GetMapping("/{id}")
    public UserResponseDTO getUserById(@PathVariable Long id) {
        return userService.getUserById(id);
    }

    @PostMapping
    public UserResponseDTO createUser(@RequestBody UserRequestDTO userRequest) {
        return userService.createUser(userRequest);
    }

    @PutMapping("/{id}")
    public UserResponseDTO updateUser(@PathVariable Long id,
                                      @RequestBody UserRequestDTO userRequest) {
        return userService.updateUser(id, userRequest);
    }

    @DeleteMapping("/{id}")
    public String deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return "User deleted successfully";
    }

    @GetMapping("/annotation/{id}")
    public Users getUserByIdWithAnnotation(@PathVariable Long id) {
        return userServiceImplWithAnnotations.getUserById(id);
    }


}
