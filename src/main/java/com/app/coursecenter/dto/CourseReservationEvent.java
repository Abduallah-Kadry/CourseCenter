package com.app.coursecenter.dto;

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
    private String studentEmail;
    private LocalDateTime eventTime = LocalDateTime.now();

    public CourseReservationEvent(String eventType, Long studentId, Long courseId, Long reservationId,
                                  String StudentEmail) {
        this.eventType = eventType;
        this.studentId = studentId;
        this.courseId = courseId;
        this.reservationId = reservationId;
        this.studentEmail = StudentEmail;

    }
}
