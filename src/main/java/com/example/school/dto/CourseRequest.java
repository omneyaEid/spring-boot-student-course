package com.example.school.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CourseRequest {
    private Long id;
    private String title;
    private String description;
}
