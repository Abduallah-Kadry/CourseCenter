package com.app.coursecenter.service;

import com.app.coursecenter.dto.CourseReservationEvent;
import com.app.coursecenter.entity.Course;
import com.app.coursecenter.entity.CourseReservation;
import com.app.coursecenter.entity.Student;
import com.app.coursecenter.repository.CourseRepository;
import com.app.coursecenter.repository.CourseReservationRepository;
import com.app.coursecenter.repository.StudentRepository;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CourseReservationConsumer {

    private final CourseReservationRepository reservationRepository;
    private final StudentRepository studentRepository;
    private final CourseRepository courseRepository;

    public CourseReservationConsumer(
            CourseReservationRepository reservationRepository,
            StudentRepository studentRepository,
            CourseRepository courseRepository
    ) {
        this.reservationRepository = reservationRepository;
        this.studentRepository = studentRepository;
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
        Student student = studentRepository.findById(event.getStudentId())
                .orElseThrow(() -> new RuntimeException("Student not found: " + event.getStudentId()));

        Course course = courseRepository.findById(event.getCourseId())
                .orElseThrow(() -> new RuntimeException("Course not found: " + event.getCourseId()));

        CourseReservation reservation = new CourseReservation();
        reservation.setStudent(student);
        reservation.setCourse(course);
        reservationRepository.save(reservation);

        System.out.println("✅ Reservation created for student " + student.getId() + ", course " + course.getId());
    }

    private void handleReservationCancelled(CourseReservationEvent event) {
        reservationRepository.findById(event.getReservationId())
                .ifPresentOrElse(reservation -> {
                    reservationRepository.delete(reservation);
                    System.out.println("🗑️ Reservation cancelled: " + event.getReservationId());
                }, () -> System.out.println("⚠️ Reservation not found: " + event.getReservationId()));
    }
}
