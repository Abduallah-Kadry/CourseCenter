package com.app.coursecenter.Service.courseservice.studentservice;

import com.app.coursecenter.entity.Student;
import com.app.coursecenter.repository.StudentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class StudentServiceImpl implements StudentService {

    final StudentRepository studentRepository;

    @Override
    public List<Student> findAll() {
        return List.of();
    }

    @Override
    public Student findById(Long id) {
        return null;
    }

    @Override
    public Student addStudent(Student student) {
        return null;
    }

    @Override
    public Student updateStudent(Long id, Student student) {
        return null;
    }

    @Override
    public void deleteStudent(Long id) {

    }
}
