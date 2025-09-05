package com.app.coursecenter.service.studentservice;

import com.app.coursecenter.entity.Student;
import com.app.coursecenter.repository.StudentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.nio.file.AccessDeniedException;
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

    @Override
    @Transactional(readOnly = true)
    public Student getUser() throws AccessDeniedException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated() ||
                authentication.getPrincipal().equals("anonymousUser")) {
            throw new AccessDeniedException("Authentication Required");
        }
        return (Student) authentication.getPrincipal();
    }
}
