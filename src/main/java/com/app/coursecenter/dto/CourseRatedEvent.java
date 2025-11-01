package com.app.coursecenter.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
public class CourseRatedEvent {

    private String eventType = "CourseRatedEvent";
    private Long studentId;
    private Long courseId;
    private Integer rate;
    private LocalDateTime ratingTime = LocalDateTime.now();
    private String studentEmail;

    public CourseRatedEvent(String eventType, Long studentId, Long courseId, int rate, String studentEmail) {
        this.eventType = eventType;
        this.studentId = studentId;
        this.courseId = courseId;
        this.rate = rate;
        this.studentEmail = studentEmail;
    }
}
