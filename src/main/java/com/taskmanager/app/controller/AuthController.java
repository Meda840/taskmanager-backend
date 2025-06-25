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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RestController
@RequestMapping("/auth")
@CrossOrigin(origins = "http://localhost:4200") // Adjust this for your frontend URL
public class AuthController {

    private static final Logger logger = LoggerFactory.getLogger(AuthController.class);
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
    public ResponseEntity<Map<String,Object>> register(@RequestBody UserRegistrationRequest request) {
        logger.info("Registering new user: email={}, username={}", request.getEmail(), request.getUsername());
       User user = userService.registerUser(request.getEmail(), request.getUsername(), request.getPassword());
        Map<String, Object> response = new HashMap<>();
        String token = jwtTokenUtil.generateToken(user.getEmail());
        user.setPassword(null);
        response.put("success", true);
        response.put("user", user);
        response.put("token", token);
        return ResponseEntity.ok(response);
        
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));

             User user = userRepository.findByEmail(request.getEmail())
                    .orElseThrow(() -> new RuntimeException("User not found"));

            String token = jwtTokenUtil.generateToken(user.getEmail());
            Map<String, Object> response = new HashMap<>();
            user.setPassword(null);
            response.put("success", true);
            response.put("user", user);
            response.put("token", token);
            return ResponseEntity.ok((response));

        }  catch (AuthenticationException ex) {
    String message = ex.getMessage(); 
    return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
            .body("Échec de l'authentification: " + message);
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
