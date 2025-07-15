package com.example.school.factory;

import com.example.school.entity.Course;
import com.example.school.entity.Student;

import java.util.List;

public class StudentFactory {
    public static Student create(String name, List<Course> courses, String ownerUsername) {
        return Student.builder()
                .name(name)
                .courses(courses)
                .ownerUsername(ownerUsername)
                .build();
    }
}