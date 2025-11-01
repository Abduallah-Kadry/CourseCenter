package com.app.coursecenter.service;

import org.springframework.stereotype.Service;

@Service
public class CourseRatingService {

    private final CourseRatingProducer courseRatingProducer;

    public CourseRatingService(CourseRatingProducer courseRatingProducer) {
        this.courseRatingProducer = courseRatingProducer;
    }

    /**
     * Called when a student rates a course.
     * Validates and then triggers the Kafka producer.
     */
    public void rateCourse(Long studentId, String studentEmail, Long courseId, int rate) {
        // Validate rating (optional but recommended)
        if (rate < 1 || rate > 5) {
            throw new IllegalArgumentException("Rate must be between 1 and 5.");
        }

        System.out.println("🎯 Processing rating request for courseId=" + courseId);

        // Send event to Kafka
        courseRatingProducer.sendCourseRatingCommand(studentId, studentEmail, courseId, rate);

        System.out.println("📤 Rating event dispatched to Kafka for studentId=" + studentId);
    }
}
