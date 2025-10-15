package com.app.coursecenter.repository;

import com.app.coursecenter.entity.CourseReservation;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CourseReservationRepository extends JpaRepository<CourseReservation, Long> {
}
