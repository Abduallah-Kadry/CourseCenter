package com.app.coursecenter.service;

import com.app.coursecenter.dto.CourseReservationEvent;
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

    public void sendReserveCourseCommand(Long studentId,String studentEmail, Long courseId) {
        CourseReservationEvent event = null;
        event = new CourseReservationEvent(
                "ReservationCreatedEvent", studentId, courseId, null, studentEmail
        );
        kafkaTemplate.send(topicName, event);
        System.out.println("📤 Sent event: " + event);
    }

    public void sendCancelReservationCommand(Long studentId, String studentEmail, Long courseId) {
        CourseReservationEvent event = null;
        event = new CourseReservationEvent(
                "ReservationCancelledEvent", studentId, courseId, null, studentEmail
        );
        kafkaTemplate.send(topicName, event);
        System.out.println("📤 Sent event: " + event);
    }
}
