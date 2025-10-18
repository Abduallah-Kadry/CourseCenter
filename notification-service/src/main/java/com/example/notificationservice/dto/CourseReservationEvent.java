package com.example.notificationservice.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
public class CourseReservationEvent {

    private String eventType; // e.g. ReservationCreatedEvent, ReservationCancelledEvent
    private Long studentId;
    private Long courseId;
    private Long reservationId;
    private LocalDateTime eventTime = LocalDateTime.now();

    public CourseReservationEvent(String eventType, Long studentId, Long courseId, Long reservationId) {
        this.eventType = eventType;
        this.studentId = studentId;
        this.courseId = courseId;
        this.reservationId = reservationId;
    }
}
