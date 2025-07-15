package com.example.school.dto;

import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StudentResponse {
    private Long id;
    private String ownerUsername;
    private List<CourseDTO> courses;
}