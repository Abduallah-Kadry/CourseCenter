package com.app.coursecenter.service.enrollmentService;


import com.app.coursecenter.entity.Course;
import com.app.coursecenter.response.CourseResponse;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface ReservationService {

    @Transactional
    List<CourseResponse> getRandomNonEnrolledCourses(int count);

    void requestCourseReservation(Long courseId);

    void cancelCourseReservation(Long reservationId);


    List<CourseResponse> getEnrolledCourses();

}
