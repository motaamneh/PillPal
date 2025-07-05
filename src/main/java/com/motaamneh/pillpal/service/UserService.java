package com.motaamneh.pillpal.service;

import com.motaamneh.pillpal.entity.User;
import com.motaamneh.pillpal.exception.EmailAlreadyExistsException;
import com.motaamneh.pillpal.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService implements UserDetailsService {
    private final UserRepository userRepository;

    public User createUser(User user) {
        if (userRepository.findByEmail(user.getEmail()).isPresent()) {
            throw new EmailAlreadyExistsException("Email already exists.");
        }
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
                    user.setName(updatedUser.getName());
                    user.setEmail(updatedUser.getEmail());
                    user.setPassword(updatedUser.getPassword());
                    return userRepository.save(user);
                }).orElseThrow(()-> new RuntimeException("User is not found"));

    }
    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }


    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User existingUser =  userRepository.findByEmail(email).orElseThrow(() -> new UsernameNotFoundException("Email not found for the email: "+email));
        return new org.springframework.security.core.userdetails.User(existingUser.getEmail(),existingUser.getPassword(), new ArrayList<>());

    }
}
