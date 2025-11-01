package com.app.coursecenter.service;

import com.app.coursecenter.dto.CourseRatedEvent;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class CourseRatingProducer {

    private final KafkaTemplate<String, CourseRatedEvent> kafkaTemplate;

    @Value("${course-rating.topic.name}")
    private String topicName;

    public CourseRatingProducer(KafkaTemplate<String, CourseRatedEvent> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    /**
     * Publishes CourseRatedEvent to Kafka topic.
     */
    public void sendCourseRatingCommand(Long studentId, String studentEmail, Long courseId, int rate) {
        CourseRatedEvent event = new CourseRatedEvent(
                "CourseRatedEvent", studentId, courseId, rate, studentEmail
        );

        kafkaTemplate.send(topicName, event);
        System.out.println("⭐ Sent CourseRatedEvent: " + event);
    }
}
