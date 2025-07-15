package com.example.school.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class CourseResponse {
    private Long id;
    private String title;
    private String description;
}