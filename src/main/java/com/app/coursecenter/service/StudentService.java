package com.app.coursecenter.service;

import com.app.coursecenter.dto.StudentDto;
import com.app.coursecenter.entity.Student;
import com.app.coursecenter.request.PasswordUpdateRequest;

import java.nio.file.AccessDeniedException;

public interface StudentService {

    StudentDto getStudentInfo() throws AccessDeniedException;

    void deleteStudent() throws AccessDeniedException;

    void updatePassword(PasswordUpdateRequest passwordUpdateRequest) throws AccessDeniedException;
}
