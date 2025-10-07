package com.app.coursecenter.repository;


import com.app.coursecenter.entity.Course;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

@Repository
public interface CourseRepository extends JpaRepository<Course, Long> {

    @Query("SELECT s.courses FROM Student s WHERE s.id = :studentId")
    Set<Course> findCoursesByStudentId(@Param("studentId") Long studentId);

}
