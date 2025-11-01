package com.app.coursecenter.response;


import lombok.Data;

@Data
public class UpdateCourseRespond {
    private String name;
    private String description;
    private Integer creditHours;
    private String imageUrl;
    private Double cost;
    private Integer rate;

}
