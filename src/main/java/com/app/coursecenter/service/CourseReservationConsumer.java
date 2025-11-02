package com.app.coursecenter.service;

import com.app.coursecenter.dto.CourseReservationEvent;
import com.app.coursecenter.entity.Course;
import com.app.coursecenter.entity.CourseReservation;
import com.app.coursecenter.entity.User;
import com.app.coursecenter.repository.CourseRepository;
import com.app.coursecenter.repository.CourseReservationRepository;
import com.app.coursecenter.repository.UserRepository;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;


@Service
public class CourseReservationConsumer {

    private final CourseReservationRepository reservationRepository;
    private final UserRepository userRepository;
    private final CourseRepository courseRepository;

    public CourseReservationConsumer(
            CourseReservationRepository reservationRepository,
            UserRepository userRepository,
            CourseRepository courseRepository
    ) {
        this.reservationRepository = reservationRepository;
        this.userRepository = userRepository;
        this.courseRepository = courseRepository;
    }


    @KafkaListener(
            topics = "${course-reservation.topic.name}",
            groupId = "${spring.kafka.consumer.group-id}"
    )
    @Transactional
    public void consumeReservationEvent(CourseReservationEvent event) {
        System.out.println("📥 Received event: " + event);

        switch (event.getEventType()) {
            case "ReservationCreatedEvent" -> handleReservationCreated(event);
            case "ReservationCancelledEvent" -> handleReservationCancelled(event);
            default -> System.out.println("⚠️ Unknown event type: " + event.getEventType());
        }
    }

    private void handleReservationCreated(CourseReservationEvent event) {
        User user = userRepository.findById(event.getStudentId())
                .orElseThrow(() -> new RuntimeException("Student not found: " + event.getStudentId()));

        Course course = courseRepository.findById(event.getCourseId())
                .orElseThrow(() -> new RuntimeException("Course not found: " + event.getCourseId()));

        CourseReservation reservation = new CourseReservation();
        reservation.setUser(user);
        reservation.setCourse(course);
        reservationRepository.save(reservation);

        System.out.println("✅ Reservation created for student " + user.getId() + ", course " + course.getId());
    }

    private void handleReservationCancelled(CourseReservationEvent event) {
        reservationRepository.findByUserIdAndCourseId(event.getStudentId(), event.getCourseId())
                .ifPresentOrElse(reservation -> {
                    // Check if the reservation is older than 30 days
                    if (reservation.getReservationTime().isBefore(LocalDateTime.now().minusDays(30))) {
                        throw new IllegalStateException("❌ Cannot cancel reservation after 30 days.");
                    }

                    reservationRepository.delete(reservation);
                    System.out.println("🗑️ Reservation cancelled successfully for Student ID: "
                            + event.getStudentId() + ", Course ID: " + event.getCourseId());
                }, () -> System.out.println("⚠️ Reservation not found for Student ID: "
                        + event.getStudentId() + ", Course ID: " + event.getCourseId()));
    }


}
