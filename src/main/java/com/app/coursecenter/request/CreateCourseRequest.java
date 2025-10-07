package com.app.coursecenter.request;


import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class CreateCourseRequest {
    @NotEmpty(message = "Course name is mandatory")
    @Size(min = 3, max = 30, message = "First name must be at least 3 characters long")
    private String name;

    @NotEmpty(message = "First Description is mandatory")
    @Size(min = 1, max = 255, message = "")
    private String description;

    @NotEmpty(message = "Credit Hours is mandatory")
    @Size(min = 1, max = 30, message = "Credit Hours must be at least 1 hour")
    private String creditHours;
}
