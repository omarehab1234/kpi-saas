package com.yourapp.dto.auth;

import java.util.UUID;

public class LoginResponse {

    private final UUID userId;
    private final String firstName;
    private final String lastName;
    private final String email;
    private final String role;
    private final String message;

    public LoginResponse(UUID userId, String firstName, String lastName,
                        String email, String role, String message) {
        this.userId = userId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.role = role;
        this.message = message;
    }

    public UUID getUserId() {
        return userId;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getEmail() {
        return email;
    }

    public String getRole() {
        return role;
    }

    public String getMessage() {
        return message;
    }
}