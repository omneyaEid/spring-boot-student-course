package com.example.school.controller;

import com.example.school.dto.CourseRequest;
import com.example.school.dto.CourseResponse;
import com.example.school.service.CourseService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/courses")
@RequiredArgsConstructor
public class CourseController {

    private final CourseService courseService;

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<CourseResponse> createCourse(@RequestBody CourseRequest request) {
        return ResponseEntity.ok(courseService.createCourse(request));
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'STUDENT')")
    public ResponseEntity<List<CourseResponse>> getAllCourses() {
        return ResponseEntity.ok(courseService.getAllCourses());
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteCourse(@PathVariable Long id) {
        courseService.deleteCourse(id);
        return ResponseEntity.noContent().build();
    }
}
