package com.example.notificationservice.service;

import com.example.notificationservice.dto.CourseReservationEvent;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;


@Service
public class NotificationConsumer {

    private final ObjectMapper objectMapper;
    private final JavaMailSender mailSender;

    public NotificationConsumer(ObjectMapper objectMapper, JavaMailSender mailSender) {
        this.objectMapper = objectMapper;
        this.mailSender = mailSender;
    }

    @KafkaListener(topics = "course-reservation-events", groupId = "notification-service-group")
    public void consumeReservationEvent(String message) {
        try {
            CourseReservationEvent event = objectMapper.readValue(message, CourseReservationEvent.class);

            switch (event.getEventType()) {
                case "ReservationCreatedEvent" ->
                        sendEmail(event, "Course Reservation Confirmed", buildReservationMessage(event, "confirmed"));
                case "ReservationCancelledEvent" ->
                        sendEmail(event, "Course Reservation Cancelled", buildReservationMessage(event, "cancelled"));
                default ->
                        System.err.println("Unknown event type: " + event.getEventType());
            }

        } catch (Exception e) {
            System.err.println("Failed to process event: " + e.getMessage());
        }
    }

    private void sendEmail(CourseReservationEvent event, String subject, String body) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(event.getStudentEmail());
        message.setSubject(subject);
        message.setText(body);
        mailSender.send(message);
    }

    private String buildReservationMessage(CourseReservationEvent event, String status) {
        return String.format(
                """
                Hello,

                Your course reservation has been %s successfully.

                Details:
                - Reservation ID: %d
                - Course ID: %d
                - Student ID: %d
                - Date: %s

                Thank you for using our course system.
                """,
                status,
                event.getReservationId(),
                event.getCourseId(),
                event.getStudentId(),
                event.getEventTime()
        );
    }
}
