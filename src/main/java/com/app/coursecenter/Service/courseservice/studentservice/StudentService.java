package com.app.coursecenter.Service.courseservice.studentservice;


import com.app.coursecenter.entity.Student;

import java.util.List;

public interface StudentService {
    List<Student> findAll();

    Student findById(Long id);

    Student addStudent(Student student);

    Student updateStudent(Long id, Student student);

    void deleteStudent(Long id);
}
