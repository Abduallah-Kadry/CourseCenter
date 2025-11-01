package com.app.coursecenter.service;

import com.app.coursecenter.dto.CourseRatedEvent;
import com.app.coursecenter.dto.CourseReservationEvent;
import com.app.coursecenter.repository.CourseReservationRepository;
import com.app.coursecenter.util.FindAuthenticatedStudent;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.nio.file.AccessDeniedException;

@Service
public class CourseReservationService {


    private final KafkaTemplate<String, CourseReservationEvent> kafkaTemplate;

    public CourseReservationService(KafkaTemplate<String, CourseReservationEvent> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void reserveCourse(Long studentId, String studentEmail, Long courseId) {
        CourseReservationEvent event = null;
        event = new CourseReservationEvent(
                "ReservationCreatedEvent", studentId, courseId, null, studentEmail
        );
        kafkaTemplate.send("course-reservation-events", event);
    }

    public void cancelReservation(Long reservationId, String studentEmail) {
        CourseReservationEvent event = null;
        event = new CourseReservationEvent(
                "ReservationCancelledEvent", null, null, reservationId, studentEmail
        );
        kafkaTemplate.send("course-reservation-events", event);
    }
}
