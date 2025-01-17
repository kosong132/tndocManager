package com.example.model;

public class Login {
    private final String username;
    private final String password;

    // Constructor
    public Login(String username, String password) {
        this.username = username;
        this.password = password;
    }

    // Getters
    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }
}
