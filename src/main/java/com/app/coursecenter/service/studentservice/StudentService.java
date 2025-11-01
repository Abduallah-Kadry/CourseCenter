package com.app.coursecenter.service.studentservice;

import com.app.coursecenter.dto.StudentDto;
import com.app.coursecenter.request.PasswordUpdateRequest;
import com.app.coursecenter.response.UserCoursesRespond;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;

import java.nio.file.AccessDeniedException;
import java.util.List;

public interface StudentService {


    // TODO get single student (search for a way to make api search by some criteria (like name, phone, etc...))

    StudentDto getStudentInfo() throws AccessDeniedException;

    void deleteStudent() throws AccessDeniedException;

    void updatePassword(PasswordUpdateRequest passwordUpdateRequest) throws AccessDeniedException;

    void requestCourseReservation(Long courseId);

    void cancelCourseReservation(Long reservationId);

    void rateCourse(Long courseId, int rate);

    List<UserCoursesRespond> getEnrolledCourses();
}
