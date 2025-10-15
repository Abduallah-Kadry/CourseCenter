package com.app.coursecenter.service;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import com.app.coursecenter.service.CourseReservationService;

public class ReserveCourseCommand implements Command {

    private final Long studentId;
    private final Long courseId;
    private final CourseReservationService reservationService;

    public ReserveCourseCommand(Long studentId, Long courseId, CourseReservationService reservationService) {
        this.studentId = studentId;
        this.courseId = courseId;
        this.reservationService = reservationService;
    }

    @Override
    public void execute() {
        reservationService.reserveCourse(studentId, courseId);
    }
}
