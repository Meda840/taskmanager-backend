package com.taskmanager.app.controller;

import com.taskmanager.app.dto.LoginRequest;
import com.taskmanager.app.dto.LoginResponse;
import com.taskmanager.app.dto.UserRegistrationRequest;
import com.taskmanager.app.model.User;
import com.taskmanager.app.repository.UserRepository;
import com.taskmanager.app.service.UserService;
import com.taskmanager.app.util.JwtTokenUtil;

import java.util.Map;
import java.util.HashMap;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.core.AuthenticationException;

@RestController
@RequestMapping("/auth")
@CrossOrigin(origins = "http://localhost:4200") // Adjust this for your frontend URL
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final JwtTokenUtil jwtTokenUtil;
    private final UserRepository userRepository;
    private final UserService userService;

    public AuthController(AuthenticationManager authenticationManager,
                          JwtTokenUtil jwtTokenUtil,
                          UserRepository userRepository,
                          UserService userService) {
        this.authenticationManager = authenticationManager;
        this.jwtTokenUtil = jwtTokenUtil;
        this.userRepository = userRepository;
        this.userService = userService;
    }

    @PostMapping("/register")
    public String register(@RequestBody UserRegistrationRequest request) {
        userService.registerUser(request.getUsername(), request.getPassword());
        return "User registered successfully";
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword()));

            User user = userRepository.findByUsername(request.getUsername())
                    .orElseThrow(() -> new RuntimeException("User not found"));

            String token = jwtTokenUtil.generateToken(user.getUsername());
            return ResponseEntity.ok(new LoginResponse(token));

        } catch (AuthenticationException ex) {
            // Mauvais username ou password
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Nom d'utilisateur ou mot de passe incorrect");
        }
    }

    //  handle RuntimeException
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<Map<String, String>> handleRuntimeException(RuntimeException ex) {
        Map<String, String> error = new HashMap<>();
        error.put("error", ex.getMessage()); 
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }
}
