package com.app.coursecenter.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.util.Set;


@Entity
@Data
public class Course {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String description;
    private int creditHours;

    @ManyToMany(mappedBy = "courses")
    private Set<Student> students;



}
