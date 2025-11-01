package com.app.coursecenter.dto;

import jakarta.persistence.Version;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor

public class CourseDto {
    private Long id;
    private String name;
    private String description;
    private int creditHours;

    @Version
    private int version;
}
