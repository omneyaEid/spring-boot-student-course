package com.example.school.dto;

import lombok.*;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AuthRequest {
    private String username;
    private String password;
}
