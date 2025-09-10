package com.app.coursecenter.service.adminservice;

import com.app.coursecenter.dto.StudentDto;

import java.util.List;

public interface AdminService {

    List<StudentDto> getAllStudents();

    StudentDto promoteToAdmin(long studentId);

    void deleteNonAdminUser(long id);
}
