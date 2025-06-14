package com.taskmanager.app.dto;

public class LoginResponse {
    private String token;

    // No-args constructor
    public LoginResponse() {}

    // Constructor with token
    public LoginResponse(String token) {
        this.token = token;
    }

    // Getter and Setter
    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
