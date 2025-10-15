package com.app.coursecenter.service;

import com.app.coursecenter.dto.CourseReservationEvent;
import com.app.coursecenter.repository.CourseReservationRepository;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class CourseReservationService {

    private final CourseReservationRepository reservationRepository;
    private final KafkaTemplate<String, CourseReservationEvent> kafkaTemplate;

    public CourseReservationService(CourseReservationRepository reservationRepository,
                                    KafkaTemplate<String, CourseReservationEvent> kafkaTemplate) {
        this.reservationRepository = reservationRepository;
        this.kafkaTemplate = kafkaTemplate;
    }

    public void reserveCourse(Long studentId, Long courseId) {
        CourseReservationEvent event = new CourseReservationEvent(
                "ReservationCreatedEvent", studentId, courseId, null
        );
        kafkaTemplate.send("course-reservation-events", event);
    }

    public void cancelReservation(Long reservationId) {
        CourseReservationEvent event = new CourseReservationEvent(
                "ReservationCancelledEvent", null, null, reservationId
        );
        kafkaTemplate.send("course-reservation-events", event);
    }
}
