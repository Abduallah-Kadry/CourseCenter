package com.app.coursecenter.service;

import com.app.coursecenter.service.CourseReservationService;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class CommandListener {

    private final ObjectMapper objectMapper;
    private final CourseReservationService reservationService;

    public CommandListener(ObjectMapper objectMapper, CourseReservationService reservationService) {
        this.objectMapper = objectMapper;
        this.reservationService = reservationService;
    }

    @KafkaListener(topics = "course-reservation-commands", groupId = "reservation-service")
    public void handleCommand(String message) throws Exception {
        JsonNode node = objectMapper.readTree(message);
        String type = node.get("type").asText();

        Command command = switch (type) {
            case "ReserveCourseCommand" ->
                    new ReserveCourseCommand(node.get("studentId").asLong() ,node.get("courseId").asLong() ,node.get("studentEmail").toString(),reservationService);
            case "CancelReservationCommand" ->
                    new CancelReservationCommand(node.get("reservationId").asLong(), reservationService, node.get("studentEmail").toString());
            default -> throw new IllegalArgumentException("Unknown command type: " + type);
        };
        command.execute();
    }
}

