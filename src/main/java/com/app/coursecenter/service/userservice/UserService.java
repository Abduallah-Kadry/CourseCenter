package com.app.coursecenter.service.userservice;

import com.app.coursecenter.dto.UserDto;
import com.app.coursecenter.request.PasswordUpdateRequest;
import com.app.coursecenter.response.UserCoursesRespond;

import java.nio.file.AccessDeniedException;
import java.util.List;

public interface UserService {


    // TODO get single student (search for a way to make api search by some criteria (like name, phone, etc...))

    UserDto getUserInfo() throws AccessDeniedException;

    void deleteUser() throws AccessDeniedException;

    void updatePassword(PasswordUpdateRequest passwordUpdateRequest) throws AccessDeniedException;

    void requestCourseReservation(Long courseId);

    void cancelCourseReservation(Long reservationId);

    void rateCourse(Long courseId, int rate);

    List<UserCoursesRespond> getEnrolledCourses();
}
