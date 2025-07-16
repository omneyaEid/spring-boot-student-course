package com.example.school.service;

import com.example.school.dto.AuthRequest;
import com.example.school.entity.Student;
import com.example.school.entity.User;
import com.example.school.exception.DuplicateUsernameException;
import com.example.school.exception.InvalidPasswordException;
import com.example.school.repository.StudentRepository;
import com.example.school.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final StudentRepository studentRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public void registerUser(AuthRequest request) {
        validateRegistration(request);

        User user = createUser(request);
        User savedUser = userRepository.save(user);

        createStudentProfile(savedUser);
    }

    public User authenticateUser(AuthRequest request) {
        return userRepository.findByUsername(request.getUsername())
                .filter(user -> passwordEncoder.matches(request.getPassword(), user.getPassword()))
                .orElseThrow(() -> new RuntimeException("Invalid credentials"));
    }

    private void validateRegistration(AuthRequest request) {
        if (userRepository.findByUsername(request.getUsername()).isPresent()) {
            throw new DuplicateUsernameException("Username already exists");
        }

        if (!isValidPassword(request.getPassword())) {
            throw new InvalidPasswordException("Password does not meet requirements");
        }
    }

    private User createUser(AuthRequest request) {
        return User.builder()
                .username(request.getUsername())
                .password(passwordEncoder.encode(request.getPassword()))
                .role("STUDENT")
                .build();
    }

    private void createStudentProfile(User user) {
        Student student = Student.builder()
                .owner(user)
                .build();
        studentRepository.save(student);
    }

    private boolean isValidPassword(String password) {
        if (password == null || password.length() < 8) {
            return false;
        }

        boolean hasUppercase = password.chars().anyMatch(Character::isUpperCase);
        boolean hasLowercase = password.chars().anyMatch(Character::isLowerCase);
        boolean hasDigit = password.chars().anyMatch(Character::isDigit);

        return hasUppercase && hasLowercase && hasDigit;
    }
}
