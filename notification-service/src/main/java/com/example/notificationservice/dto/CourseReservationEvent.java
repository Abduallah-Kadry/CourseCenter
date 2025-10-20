package com.example.notificationservice.dto;

import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@NoArgsConstructor
@Data
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

    public String getEventType() {
        return eventType;
    }

    public void setEventType(String eventType) {
        this.eventType = eventType;
    }

    public Long getStudentId() {
        return studentId;
    }

    public void setStudentId(Long studentId) {
        this.studentId = studentId;
    }

    public Long getCourseId() {
        return courseId;
    }

    public void setCourseId(Long courseId) {
        this.courseId = courseId;
    }

    public Long getReservationId() {
        return reservationId;
    }

    public void setReservationId(Long reservationId) {
        this.reservationId = reservationId;
    }

    public String getStudentEmail() {
        return studentEmail;
    }

    public void setStudentEmail(String studentEmail) {
        this.studentEmail = studentEmail;
    }

    public LocalDateTime getEventTime() {
        return eventTime;
    }

}
