package com.motaamneh.pillpal.controller;

import com.motaamneh.pillpal.entity.User;
import com.motaamneh.pillpal.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @GetMapping("/{id}")
    public Optional<User> getUserById(@PathVariable Long id) {
        return userService.getUserById(id);
    }
    @GetMapping
    public List<User> getAllUsers() {
        return userService.getAllUsers();
    }
    @PostMapping
    public User createUser(@RequestBody User user) {
        return userService.createUser(user);
    }
    @PutMapping("/{id}")
    public User updateUser(@PathVariable Long id, @RequestBody User user) {
        return userService.updateUser(id, user);
    }
    @DeleteMapping("/{id}")
    public void deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
    }
}
