package com.app.coursecenter.service;

import com.app.coursecenter.entity.Course;
import com.app.coursecenter.repository.CourseReservationRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CourseRatingService {

    private final CourseRatingProducer courseRatingProducer;
    private final CourseReservationRepository reservationRepository;

    public CourseRatingService(CourseRatingProducer courseRatingProducer, CourseReservationRepository reservationRepository) {
        this.courseRatingProducer = courseRatingProducer;
        this.reservationRepository = reservationRepository;
    }

    public Double getAverageCourseRate(Long courseId) {
        Double avg = reservationRepository.findAverageRateByCourseId(courseId);
        return avg != null ? avg : 0.0; // return 0.0 if no ratings yet
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
