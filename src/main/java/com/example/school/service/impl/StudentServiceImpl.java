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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class StudentServiceImpl implements StudentService {

    private static final Logger logger = LoggerFactory.getLogger(StudentServiceImpl.class);

    private final StudentRepository studentRepository;
    private final CourseRepository courseRepository;
    private final UserRepository userRepository;

    @Override
    public StudentResponse getStudent(Long id, String requesterUsername) {
        logger.info("Fetching student with ID: {} for user: {}", id, requesterUsername);
        Student student = getOwnedStudentOrThrow(id, requesterUsername);
        return MapperUtil.toStudentResponse(student);
    }

    @Override
    public StudentResponse getStudentById(Long id) {
        logger.info("Fetching student by ID: {}", id);
        return MapperUtil.toStudentResponse(
                studentRepository.findById(id)
                        .orElseThrow(() -> {
                            logger.error("Student not found with ID: {}", id);
                            return new ResourceNotFoundException("Student not found");
                        })
        );
    }

    @Override
    public StudentResponse getStudentByUsername(String username) {
        logger.info("Fetching student for username: {}", username);
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> {
                    logger.error("User  not found with username: {}", username);
                    return new ResourceNotFoundException("User  not found");
                });

        Student student = studentRepository.findByOwner(user)
                .orElseThrow(() -> {
                    logger.error("Student not found for user: {}", username);
                    return new ResourceNotFoundException("Student not found");
                });

        return MapperUtil.toStudentResponse(student);
    }

    @Override
    public List<StudentResponse> getAllStudents() {
        logger.info("Fetching all students");
        return studentRepository.findAll()
                .stream()
                .map(MapperUtil::toStudentResponse)
                .toList();
    }

    @Override
    public StudentResponse addCoursesToStudent(String username, List<Long> courseIds) {
        logger.info("Adding courses to student with username: {}", username);
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> {
                    logger.error("User  not found with username: {}", username);
                    return new ResourceNotFoundException("User  not found");
                });

        Student student = studentRepository.findByOwner(user)
                .orElseThrow(() -> {
                    logger.error("Student not found for user: {}", username);
                    return new ResourceNotFoundException("Student not found");
                });

        List<Course> newCourses = courseRepository.findAllById(courseIds);

        if (newCourses.size() != courseIds.size()) {
            logger.error("One or more course IDs are invalid: {}", courseIds);
            throw new ResourceNotFoundException("One or more course IDs are invalid.");
        }

        if (student.getCourses() == null || student.getCourses().isEmpty()) {
            logger.info("Setting courses for the first time for student: {}", username);
            student.setCourses(newCourses);
        } else {
            List<Course> updatedCourses = student.getCourses();
            newCourses.stream()
                    .filter(course -> !updatedCourses.contains(course))
                    .forEach(updatedCourses::add);
            logger.info("Updated courses for student: {}", username);
        }

        studentRepository.save(student);
        return MapperUtil.toStudentResponse(student);
    }

    @Override
    public StudentResponse enrollCoursesByAdmin(Long studentId, List<Long> courseIds) {
        logger.info("Admin enrolling courses for student ID: {}", studentId);
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> {
                    logger.error("Student not found with ID: {}", studentId);
                    return new ResourceNotFoundException("Student not found");
                });

        List<Course> newCourses = courseRepository.findAllById(courseIds);

        if (newCourses.size() != courseIds.size()) {
            logger.error("One or more course IDs are invalid: {}", courseIds);
            throw new ResourceNotFoundException("One or more course IDs are invalid.");
        }

        if (student.getCourses() == null || student.getCourses().isEmpty()) {
            logger.info("Setting courses for the first time for student ID: {}", studentId);
            student.setCourses(newCourses);
        } else {
            List<Course> updatedCourses = student.getCourses();
            newCourses.stream()
                    .filter(course -> !updatedCourses.contains(course))
                    .forEach(updatedCourses::add);
            logger.info("Updated courses for student ID: {}", studentId);
        }

        studentRepository.save(student);
        return MapperUtil.toStudentResponse(student);
    }

    private Student getOwnedStudentOrThrow(Long id, String username) {
        logger.info("Checking ownership of student ID: {} for user: {}", id, username);
        Student student = studentRepository.findById(id)
                .orElseThrow(() -> {
                    logger.error("Student not found with ID: {}", id);
                    return new ResourceNotFoundException("Student not found");
                });

        if (!student.getOwner().getUsername().equals(username)) {
            logger.error("Access denied for user: {} on student ID: {}", username, id);
            throw new AccessDeniedException("You are not authorized to access this student");
        }

        return student;
    }

    @Override
    public StudentResponse removeCourseForStudent(String username, Long courseId) {
        logger.info("Removing course ID: {} for student with username: {}", courseId, username);
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> {
                    logger.error("User  not found with username: {}", username);
                    return new ResourceNotFoundException("User  not found");
                });

        Student student = studentRepository.findByOwner(user)
                .orElseThrow(() -> {
                    logger.error("Student not found for user: {}", username);
                    return new ResourceNotFoundException("Student not found");
                });

        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> {
                    logger.error("Course not found with ID: {}", courseId);
                    return new ResourceNotFoundException("Course not found");
                });

        if (!student.getCourses().remove(course)) {
            logger.error("Course ID: {} not found in student's list", courseId);
            throw new ResourceNotFoundException("Course not found in student's list");
        }

        studentRepository.save(student);
        logger.info("Course ID: {} removed for student with username: {}", courseId, username);
        return MapperUtil.toStudentResponse(student);
    }

    @Override
    public StudentResponse removeCourseByAdmin(Long studentId, Long courseId) {
        logger.info("Admin removing course ID: {} for student ID: {}", courseId, studentId);
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> {
                    logger.error("Student not found with ID: {}", studentId);
                    return new ResourceNotFoundException("Student not found");
                });

        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> {
                    logger.error("Course not found with ID: {}", courseId);
                    return new ResourceNotFoundException("Course not found");
                });

        if (!student.getCourses().remove(course)) {
            logger.error("Course ID: {} not found in student's list", courseId);
            throw new ResourceNotFoundException("Course not found in student's list");
        }

        studentRepository.save(student);
        logger.info("Course ID: {} removed for student ID: {}", courseId, studentId);
        return MapperUtil.toStudentResponse(student);
    }
}
