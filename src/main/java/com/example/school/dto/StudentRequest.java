package com.example.school.dto;

import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StudentRequest {
    private List<Long> courseIds;
}