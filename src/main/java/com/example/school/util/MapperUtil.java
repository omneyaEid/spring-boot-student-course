package com.example.school.util;

import com.example.school.dto.CourseDTO;
import com.example.school.dto.CourseResponse;
import com.example.school.dto.StudentResponse;
import com.example.school.entity.Course;
import com.example.school.entity.Student;

import java.util.List;

public class MapperUtil {

    public static StudentResponse toStudentResponse(Student student) {
        return StudentResponse.builder()
                .id(student.getId())
                .ownerUsername(student.getOwner().getUsername())
                .courses(toCourseDTOs(student.getCourses()))
                .build();
    }

    private static List<CourseDTO> toCourseDTOs(List<Course> courses) {
        return courses.stream()
                .map(course -> CourseDTO.builder()
                        .id(course.getId())
                        .title(course.getTitle())
                        .description(course.getDescription())
                        .build())
                .toList();
    }
    public static CourseResponse toCourseResponse(Course course) {
        return CourseResponse.builder()
                .id(course.getId())
                .title(course.getTitle())
                .description(course.getDescription())
                .build();
    }
}