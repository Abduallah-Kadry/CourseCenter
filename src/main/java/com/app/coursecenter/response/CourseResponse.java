package com.app.coursecenter.response;


import lombok.Data;

@Data
public class CourseResponse {
    private Integer id;
    private String name;
    private String description;
    private int creditHours;
    private String imageUrl;
    private double cost;
    private Integer rate;

}
