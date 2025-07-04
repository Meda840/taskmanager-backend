package com.taskmanager.app.dto;

public class UserRegistrationRequest {
    private String username;
    private String email;
    private String password;

    public UserRegistrationRequest() {}

    public UserRegistrationRequest(String email, String username, String password) {
         this.email = email;
        this.username = username;
        this.password = password;
        
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email){
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
