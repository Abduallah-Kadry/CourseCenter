package com.app.coursecenter.request;


import com.app.coursecenter.Annotaions.ValidImageFile;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class UpdateCourseRequest {
    @Size(min = 3, max = 30, message = "First name must be at least 3 characters long")
    private String name;

    @Size(min = 1, max = 255, message = "")
    private String description;

    @Size(min = 1, max = 30, message = "Credit Hours must be at least 1 hour")
    private String creditHours;


    @Min(value = 0,message = "Cost must equal or be more than zero")
    private double cost;

    @ValidImageFile(allowEmpty = true)
    private MultipartFile imageFile;

    private String imageUrl;

    @Min(1) @Max(5)
    private Integer rate;
}
