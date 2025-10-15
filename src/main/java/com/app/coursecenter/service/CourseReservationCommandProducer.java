package com.app.coursecenter.service;

import com.app.coursecenter.dto.CourseReservationEvent;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class CourseReservationCommandProducer {

    private final KafkaTemplate<String, CourseReservationEvent> kafkaTemplate;

    @Value("${course-reservation.topic.name}")
    private String topicName;

    public CourseReservationCommandProducer(KafkaTemplate<String, CourseReservationEvent> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void sendReserveCourseCommand(Long studentId, Long courseId) {
        CourseReservationEvent event = new CourseReservationEvent(
                "ReservationCreatedEvent", studentId, courseId, null
        );
        kafkaTemplate.send(topicName, event);
        System.out.println("📤 Sent event: " + event);
    }

    public void sendCancelReservationCommand(Long reservationId, Long studentId, Long courseId) {
        CourseReservationEvent event = new CourseReservationEvent(
                "ReservationCancelledEvent", studentId, courseId, reservationId
        );
        kafkaTemplate.send(topicName, event);
        System.out.println("📤 Sent event: " + event);
    }
}
