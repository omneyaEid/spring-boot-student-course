package com.example.school.service;

import com.example.school.dto.StudentRequest;
import com.example.school.dto.StudentResponse;

import java.util.List;

public interface StudentService {

    StudentResponse getStudent(Long id, String requesterUsername);

    StudentResponse getStudentById(Long id);

    StudentResponse getStudentByUsername(String username);

    List<StudentResponse> getAllStudents();

    StudentResponse enrollCoursesByAdmin(Long studentId, List<Long> courseIds);

    StudentResponse addCoursesToStudent(String username, List<Long> courseIds);

    StudentResponse removeCourseForStudent(String username, Long courseId);

    StudentResponse removeCourseByAdmin(Long studentId, Long courseId);
}