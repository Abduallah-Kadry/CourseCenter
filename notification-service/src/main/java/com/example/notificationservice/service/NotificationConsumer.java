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
                case "ReservationCreatedEvent" -> sendEmail(event, "A7A Reservation Confirmed!");
                case "ReservationCancelledEvent" -> sendEmail(event, "A7A Reservation Cancelled!");
            }

        } catch (Exception e) {
            System.err.println("Failed to process event: " + e.getMessage());
        }
    }

    private void sendEmail(CourseReservationEvent event, String subject) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo("abdullahibrahimothman@gmail.com"); // TODO: fetch from DB
        message.setSubject(subject);
        message.setText("Your reservation event: " + event);
        mailSender.send(message);
    }
}

