package com.example.school.service;

import com.example.school.dto.CourseRequest;
import com.example.school.dto.CourseResponse;

import java.util.List;

public interface CourseService {
    CourseResponse createCourse(CourseRequest request);
    List<CourseResponse> getAllCourses();
    void deleteCourse(Long courseId);
}