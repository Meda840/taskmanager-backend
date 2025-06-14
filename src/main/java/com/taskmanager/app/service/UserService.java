package com.taskmanager.app.service;

import com.taskmanager.app.model.User;
import com.taskmanager.app.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository,
                       PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public User registerUser(String username, String rawPassword) {
        // Check if user exists
        if (userRepository.findByUsername(username).isPresent()) {
            throw new RuntimeException("Username already taken");
        }
        // Encode password
        String encodedPassword = passwordEncoder.encode(rawPassword);
        User user = new User();
        user.setUsername(username);
        user.setPassword(encodedPassword);
        return userRepository.save(user);
    }
}
