package com.example.school.service.impl;

import com.example.school.dto.StudentResponse;
import com.example.school.entity.Course;
import com.example.school.entity.Student;
import com.example.school.entity.User;
import com.example.school.exception.ResourceNotFoundException;
import com.example.school.repository.CourseRepository;
import com.example.school.repository.StudentRepository;
import com.example.school.repository.UserRepository;
import com.example.school.service.StudentService;
import com.example.school.util.MapperUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class StudentServiceImpl implements StudentService {

    private final StudentRepository studentRepository;
    private final CourseRepository courseRepository;
    private final UserRepository userRepository;

    @Override
    public StudentResponse getStudent(Long id, String requesterUsername) {
        Student student = getOwnedStudentOrThrow(id, requesterUsername);
        return MapperUtil.toStudentResponse(student);
    }

    @Override
    public StudentResponse getStudentById(Long id) {
        return MapperUtil.toStudentResponse(
                studentRepository.findById(id)
                        .orElseThrow(() -> new ResourceNotFoundException("Student not found"))
        );
    }

    @Override
    public StudentResponse getStudentByUsername(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        Student student = studentRepository.findByOwner(user)
                .orElseThrow(() -> new ResourceNotFoundException("Student not found"));

        return MapperUtil.toStudentResponse(student);
    }

    @Override
    public List<StudentResponse> getAllStudents() {
        return studentRepository.findAll()
                .stream()
                .map(MapperUtil::toStudentResponse)
                .toList();
    }

    @Override
    public StudentResponse addCoursesToStudent(String username, List<Long> courseIds) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        Student student = studentRepository.findByOwner(user)
                .orElseThrow(() -> new ResourceNotFoundException("Student not found"));

        List<Course> newCourses = courseRepository.findAllById(courseIds);

        if (newCourses.size() != courseIds.size()) {
            throw new ResourceNotFoundException("One or more course IDs are invalid.");
        }

        if (student.getCourses() == null || student.getCourses().isEmpty()) {
            // First time setting courses
            student.setCourses(newCourses);
        } else {
            List<Course> updatedCourses = student.getCourses();
            newCourses.stream()
                    .filter(course -> !updatedCourses.contains(course))
                    .forEach(updatedCourses::add);
        }

        studentRepository.save(student);
        return MapperUtil.toStudentResponse(student);
    }

    @Override
    public StudentResponse enrollCoursesByAdmin(Long studentId, List<Long> courseIds) {
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new ResourceNotFoundException("Student not found"));

        List<Course> newCourses = courseRepository.findAllById(courseIds);

        if (newCourses.size() != courseIds.size()) {
            throw new ResourceNotFoundException("One or more course IDs are invalid.");
        }

        if (student.getCourses() == null || student.getCourses().isEmpty()) {
            student.setCourses(newCourses);
        } else {
            List<Course> updatedCourses = student.getCourses();
            newCourses.stream()
                    .filter(course -> !updatedCourses.contains(course))
                    .forEach(updatedCourses::add);
        }

        studentRepository.save(student);
        return MapperUtil.toStudentResponse(student);
    }

    private Student getOwnedStudentOrThrow(Long id, String username) {
        Student student = studentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Student not found"));

        if (!student.getOwner().getUsername().equals(username)) {
            throw new AccessDeniedException("You are not authorized to access this student");
        }

        return student;
    }

    @Override
    public StudentResponse removeCourseForStudent(String username, Long courseId) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        Student student = studentRepository.findByOwner(user)
                .orElseThrow(() -> new ResourceNotFoundException("Student not found"));

        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new ResourceNotFoundException("Course not found"));

        if (!student.getCourses().remove(course)) {
            throw new ResourceNotFoundException("Course not found in student's list");
        }

        studentRepository.save(student);
        return MapperUtil.toStudentResponse(student);
    }

    @Override
    public StudentResponse removeCourseByAdmin(Long studentId, Long courseId) {
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new ResourceNotFoundException("Student not found"));

        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new ResourceNotFoundException("Course not found"));

        if (!student.getCourses().remove(course)) {
            throw new ResourceNotFoundException("Course not found in student's list");
        }

        studentRepository.save(student);
        return MapperUtil.toStudentResponse(student);
    }
}
