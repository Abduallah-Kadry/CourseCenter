package com.app.coursecenter.service.adminservice;

import com.app.coursecenter.dto.StudentDto;
import org.springframework.data.domain.Page;



public interface AdminService {

    Page<StudentDto> getAllStudents(int page, int size);

    StudentDto promoteToAdmin(long studentId);

    void deleteNonAdminUser(long id);
}
