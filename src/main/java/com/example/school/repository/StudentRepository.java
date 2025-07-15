package com.example.school.repository;

import com.example.school.entity.Student;
import com.example.school.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface StudentRepository extends JpaRepository<Student, Long> {

    Optional<Student> findByOwner(User owner);
}