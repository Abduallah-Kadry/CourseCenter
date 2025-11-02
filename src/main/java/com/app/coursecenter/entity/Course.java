package com.app.coursecenter.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Entity
@Data
public class Course extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String description;
    private String imageUrl;
    private Double cost;

    // checks may fail if mysql is less than version 8.0
    @Column(columnDefinition = "INT CHECK (rating >= 0 AND rating <= 5)")
    private Integer rating;

    @Column(name = "credit_hours", nullable = false, columnDefinition = "INT CHECK (credit_hours BETWEEN 1 AND 4)")
    private Integer creditHours;



    @Version
    private int version;
}