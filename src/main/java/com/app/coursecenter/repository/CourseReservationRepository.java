package com.app.coursecenter.repository;

import com.app.coursecenter.entity.Course;
import com.app.coursecenter.entity.CourseReservation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface CourseReservationRepository extends JpaRepository<CourseReservation, Long> {

    Optional<CourseReservation> findByUserIdAndCourseId(Long userId, Long courseId);

    @Query("SELECT AVG(r.courseRate) FROM CourseReservation r WHERE r.course.id = :courseId")
    Double findAverageRateByCourseId(@Param("courseId") Long courseId);

    @Query("SELECT r.course FROM CourseReservation r WHERE r.user.id = :userId")
    List<Course> findCoursesByUserId(@Param("userId") Long userId);
}
