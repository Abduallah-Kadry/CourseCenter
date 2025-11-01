package com.app.coursecenter.request;


import jakarta.validation.constraints.*;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class CreateCourseRequest {

    @NotEmpty(message = "Course name is mandatory")
    @Size(min = 3, max = 30, message = "First name must be at least 3 characters long")
    private String name;

    @NotEmpty(message = "First Description is mandatory")
    @Size(min = 1, max = 255, message = "")
    private String description;

    @NotNull(message = "Credit Hours is mandatory")
    @Min(value= 1, message = "Credit Hours must be at least 1 hour")
    @Max(value= 4, message = "Credit Hours must be at most 4 hour")
    private Integer creditHours;

    private String imageUrl;

    @NotNull(message = "Cost is mandatory")
    @Min(value = 0,message = "Cost must equal or be more than zero")
    private Double cost;

}
