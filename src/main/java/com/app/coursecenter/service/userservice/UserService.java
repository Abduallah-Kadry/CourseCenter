package com.app.coursecenter.service.userservice;

import com.app.coursecenter.request.PasswordUpdateRequest;
import com.app.coursecenter.response.CourseResponse;
import com.app.coursecenter.response.UserDetailResponse;
import org.springframework.data.domain.Page;
import org.springframework.transaction.annotation.Transactional;

import java.nio.file.AccessDeniedException;
import java.util.List;

public interface UserService {

    // TODO get single student (search for a way to make api search by some criteria (like name, phone, etc...))

    UserDetailResponse getUserInfo() throws AccessDeniedException;

    void deleteUser() throws AccessDeniedException;

    void updatePassword(PasswordUpdateRequest passwordUpdateRequest) throws AccessDeniedException;

    void rateCourse(Long courseId, int rate);

    Page<UserDetailResponse> getAllUser(int page, int size);

    @Transactional
    UserDetailResponse promoteToAdmin(long studentId);

    @Transactional
    void deleteNonAdminUser(long id);
}
