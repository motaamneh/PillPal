package com.motaamneh.pillpal.service;

import com.motaamneh.pillpal.entity.User;
import com.motaamneh.pillpal.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {
    private final UserRepository userRepository;
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;

    }
    public User createUser(User user) {
        return userRepository.save(user);
    }
    public Optional<User> getUserById(Long id) {
        return userRepository.findById(id);

    }
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public User updateUser(Long id,User updatedUser) {
        return userRepository.findById(id)
                .map(user -> {
                    user.setName(user.getName());
                    user.setEmail(user.getEmail());
                    user.setPassword(user.getPassword());
                    return userRepository.save(user);
                }).orElseThrow(()-> new RuntimeException("User is not found"));

    }
    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }


}
