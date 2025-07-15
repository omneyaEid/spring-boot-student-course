package com.example.school.controller;

import com.example.school.dto.StudentRequest;
import com.example.school.dto.StudentResponse;
import com.example.school.service.StudentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/students")
@RequiredArgsConstructor
public class StudentController {

    private final StudentService studentService;

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<StudentResponse>> getAllStudents() {
        return ResponseEntity.ok(studentService.getAllStudents());
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<StudentResponse> getStudentById(@PathVariable Long id) {
        return ResponseEntity.ok(studentService.getStudentById(id));
    }

    @GetMapping("/me")
    @PreAuthorize("hasRole('STUDENT')")
    public ResponseEntity<StudentResponse> getStudentByToken(Principal principal) {
        return ResponseEntity.ok(studentService.getStudentByUsername(principal.getName()));
    }

    @PostMapping("/me/courses")
    @PreAuthorize("hasRole('STUDENT')")
    public ResponseEntity<StudentResponse> addCoursesToStudent(
            @RequestBody List<Long> courseIds, Principal principal) {
        return ResponseEntity.ok(studentService.addCoursesToStudent(principal.getName(), courseIds));
    }

    @PutMapping("/{id}/courses")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<StudentResponse> enrollCoursesByAdmin(@PathVariable Long id, @RequestBody List<Long> courseIds) {
        return ResponseEntity.ok(studentService.enrollCoursesByAdmin(id, courseIds));
    }

    @DeleteMapping("/me/courses/{courseId}")
    @PreAuthorize("hasRole('STUDENT')")
    public ResponseEntity<StudentResponse> removeCourseForStudent(
            @PathVariable Long courseId,
            Principal principal
    ) {
        return ResponseEntity.ok(studentService.removeCourseForStudent(principal.getName(), courseId));
    }

    @DeleteMapping("/{studentId}/courses/{courseId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<StudentResponse> removeCourseForStudentByAdmin(
            @PathVariable Long studentId,
            @PathVariable Long courseId
    ) {
        return ResponseEntity.ok(studentService.removeCourseByAdmin(studentId, courseId));
    }
}
