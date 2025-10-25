package com.app.coursecenter.repository;

import com.app.coursecenter.entity.CourseReservation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CourseReservationRepository extends JpaRepository<CourseReservation, Long> {

    Optional<CourseReservation> findByStudentIdAndCourseId(Long studentId, Long courseId);
}
