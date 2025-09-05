package com.app.coursecenter.service.studentservice;


import com.app.coursecenter.entity.Student;

import java.nio.file.AccessDeniedException;
import java.util.List;

public interface StudentService {
    List<Student> findAll();

    Student findById(Long id);

    Student addStudent(Student student);

    Student updateStudent(Long id, Student student);

    void deleteStudent(Long id);

    Student getUser() throws AccessDeniedException;

}
