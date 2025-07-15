package com.example.school.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CourseDTO {
    private Long id;

    private String name;
    private String title;
    private String description;
}
