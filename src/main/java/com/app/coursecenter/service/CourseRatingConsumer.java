package com.app.coursecenter.service;

import com.app.coursecenter.dto.CourseRatedEvent;
import com.app.coursecenter.entity.CourseReservation;
import com.app.coursecenter.repository.CourseReservationRepository;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CourseRatingConsumer {

    private final CourseReservationRepository reservationRepository;

    public CourseRatingConsumer(CourseReservationRepository reservationRepository) {
        this.reservationRepository = reservationRepository;
    }

    // ✅ receive event directly instead of String
    @KafkaListener(topics = "course-rating-events", groupId = "rating-service-group")
    public void consumeCourseRatedEvent(CourseRatedEvent event) {
        try {
            System.out.println("📩 Received CourseRatedEvent: " + event);

            Optional<CourseReservation> reservationOpt =
                    reservationRepository.findByUserIdAndCourseId(
                            event.getStudentId(), event.getCourseId()
                    );

            if (reservationOpt.isPresent()) {
                CourseReservation reservation = reservationOpt.get();
                reservation.setCourseRate(event.getRate());
                reservation.setRatingTime(event.getRatingTime());
                reservationRepository.save(reservation);

                System.out.println("✅ Rating saved for reservation ID: " + reservation.getId());
            } else {
                System.out.println("⚠️ Reservation not found for studentId=" +
                        event.getStudentId() + " and courseId=" + event.getCourseId());
            }

        } catch (Exception e) {
            System.err.println("❌ Failed to process CourseRatedEvent: " + e.getMessage());
        }
    }
}
