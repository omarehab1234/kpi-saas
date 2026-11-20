
package com.yourapp.service;

import java.util.UUID;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.yourapp.dto.auth.LoginRequest;
import com.yourapp.dto.auth.LoginResponse;
import com.yourapp.dto.auth.RegisterRequest;
import com.yourapp.model.entity.User;
import com.yourapp.model.enums.UserStatus;
import com.yourapp.repository.UserRepository;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(
            UserRepository userRepository,
            PasswordEncoder passwordEncoder) {

        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }
    // ==============================
    // register, get, update, delete user
    // ==============================
    @Transactional
    public User register(RegisterRequest request) {

        // Check if email already exists
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("Email already registered");
        }

        // Create User entity
        User user = new User();

        // Map DTO to Entity
        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());
        user.setEmail(request.getEmail());

        // Encode password
        user.setPasswordHash(
            passwordEncoder.encode(request.getPassword())
        );

        // Save user
        return userRepository.save(user);
    }

    @Transactional(readOnly = true)
    public User getUserById(UUID id) {

        return userRepository.findById(id)
                .orElseThrow(() ->
                    new RuntimeException("User not found"));
    }

    @Transactional(readOnly = true)
    public User getUserByEmail(String email) {

        return userRepository.findByEmail(email)
                .orElseThrow(() ->
                    new RuntimeException("User not found"));
    }

    @Transactional(readOnly = true)
    public boolean emailExists(String email) {

        return userRepository.existsByEmail(email);
    }

    @Transactional
    public User updateUser(UUID id, RegisterRequest request) {

        User user = getUserById(id);

        // Check email uniqueness if changed
        if (!user.getEmail().equals(request.getEmail())
                && userRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("Email already registered");
        }

        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());
        user.setEmail(request.getEmail());

        return userRepository.save(user);
    }

    @Transactional
    public void deleteUser(UUID id) {

        User user = getUserById(id);

        userRepository.delete(user);
    }

    // ==============================
    // Authentication Login
    // ==============================
    public LoginResponse login(LoginRequest request) {

    User user = userRepository.findByEmail(request.getEmail())
            .orElseThrow(() ->
                    new RuntimeException("Invalid email or password"));

    if (user.getStatus() != UserStatus.ACTIVE) {
        throw new RuntimeException("User account is not active");
    }

    if (!passwordEncoder.matches(
            request.getPassword(),
            user.getPasswordHash())) {

        throw new RuntimeException("Invalid email or password");
    }

    return new LoginResponse(
            user.getId(),
            user.getFirstName(),
            user.getLastName(),
            user.getEmail(),
            user.getRole().name(),
            "Login successful"
        );
    }
}
