package com.example.school.service.impl;

import com.example.school.dto.CourseRequest;
import com.example.school.dto.CourseResponse;
import com.example.school.entity.Course;
import com.example.school.exception.ResourceNotFoundException;
import com.example.school.repository.CourseRepository;
import com.example.school.service.CourseService;
import com.example.school.util.MapperUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CourseServiceImpl implements CourseService {

    private final CourseRepository courseRepository;

    @Override
    @PreAuthorize("hasRole('ADMIN')")
    public CourseResponse createCourse(CourseRequest request) {
        Course course = Course.builder()
                .title(request.getTitle())
                .description(request.getDescription())
                .build();

        courseRepository.save(course);
        return MapperUtil.toCourseResponse(course);
    }

    @Override
    public List<CourseResponse> getAllCourses() {
        return courseRepository.findAll()
                .stream()
                .map(MapperUtil::toCourseResponse)
                .toList();
    }

    @Override
    @PreAuthorize("hasRole('ADMIN')")
    public void deleteCourse(Long courseId) {
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new ResourceNotFoundException("Course not found"));
        courseRepository.delete(course);
    }
}