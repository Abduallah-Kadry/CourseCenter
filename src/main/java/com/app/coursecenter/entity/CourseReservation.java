package com.app.coursecenter.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(
        name = "course_reservations",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = {"student_id", "course_id"})
        },
        indexes = {
                @Index(name = "idx_student_id", columnList = "student_id"),
                @Index(name = "idx_course_id", columnList = "course_id"),
        }
)
@Getter
@Setter
public class CourseReservation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "student_id", nullable = false)
    private Student student;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "course_id", nullable = false)
    private Course course;

    @Column(name = "reservation_time", nullable = false)
    private LocalDateTime reservationTime = LocalDateTime.now();

}