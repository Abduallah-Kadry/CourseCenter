package com.app.coursecenter.service.studentservice;

import com.app.coursecenter.dto.StudentDto;
import com.app.coursecenter.request.PasswordUpdateRequest;

import java.nio.file.AccessDeniedException;

public interface StudentService {

    // where is the fukin crud ? huh ? where is add student, update the entire student

    // get student list
    // get single student (search for a way to make api search by some criteria (like name, phone, etc...))


    StudentDto getStudentInfo() throws AccessDeniedException;

    void deleteStudent() throws AccessDeniedException;

    void updatePassword(PasswordUpdateRequest passwordUpdateRequest) throws AccessDeniedException;
}
