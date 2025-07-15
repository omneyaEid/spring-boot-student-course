package com.example.school.controller;

import com.example.school.dto.AuthRequest;
import com.example.school.entity.Student;
import com.example.school.entity.User;
import com.example.school.repository.StudentRepository;
import com.example.school.repository.UserRepository;
import com.example.school.security.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UserRepository userRepository;
    private final StudentRepository studentRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody AuthRequest request) {
        // Check if username already exists
        if (userRepository.findByUsername(request.getUsername()).isPresent()) {
            return ResponseEntity
                    .badRequest()
                    .body("Username already exists. Please choose another.");
        }

        // Validate password strength
        if (!isValidPassword(request.getPassword())) {
            return ResponseEntity
                    .badRequest()
                    .body("Password must be at least 8 characters long and include uppercase, lowercase, and a number.");
        }

        // Save user
        User user = User.builder()
                .username(request.getUsername())
                .password(passwordEncoder.encode(request.getPassword()))
                .role("STUDENT")
                .build();
        userRepository.save(user);

        // Automatically create associated Student
        studentRepository.save(Student.builder().owner(user).build());

        return ResponseEntity.ok("User registered successfully");
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody AuthRequest request) {
        User user = userRepository.findByUsername(request.getUsername())
                .orElse(null);

        if (user == null || !passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid username or password");
        }

        return ResponseEntity.ok(jwtService.generateToken(user));
    }

    private boolean isValidPassword(String password) {
        if (password == null || password.length() < 8) return false;

        boolean hasUppercase = password.chars().anyMatch(Character::isUpperCase);
        boolean hasLowercase = password.chars().anyMatch(Character::isLowerCase);
        boolean hasDigit = password.chars().anyMatch(Character::isDigit);

        return hasUppercase && hasLowercase && hasDigit;
    }
}