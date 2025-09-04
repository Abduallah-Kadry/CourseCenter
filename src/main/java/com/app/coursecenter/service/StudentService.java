package com.app.coursecenter.service;

import com.app.coursecenter.entity.Student;

import java.nio.file.AccessDeniedException;

public interface StudentService {
    Student getUser() throws AccessDeniedException;
}
