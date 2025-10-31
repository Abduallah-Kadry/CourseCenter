package com.app.coursecenter.request;


import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class UpdateCourseRequest {
    @Size(min = 3, max = 30, message = "First name must be at least 3 characters long")
    private String name;

    @Size(min = 1, max = 255, message = "")
    private String description;

    @Size(min = 1, max = 30, message = "Credit Hours must be at least 1 hour")
    private String creditHours;

    private String imageUrl;

    @Min(value = 0,message = "Cost must equal or be more than zero")
    private double cost;

    @Min(1) @Max(5)
    private Integer rate;
}
