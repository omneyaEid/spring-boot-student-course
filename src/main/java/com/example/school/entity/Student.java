package com.example.school.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class Student {

    @Id
    @GeneratedValue
    private Long id;

    private String name;

    private String ownerUsername;

    @ManyToOne
    @JoinColumn(name = "owner_id")
    private User owner;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "student_courses",
            joinColumns = @JoinColumn(name = "student_id"),
            inverseJoinColumns = @JoinColumn(name = "course_id")
    )
    @Builder.Default
    private List<Course> courses = new ArrayList<>();
}
