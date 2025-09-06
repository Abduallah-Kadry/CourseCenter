package com.app.coursecenter.util;

import com.app.coursecenter.entity.Student;

import java.nio.file.AccessDeniedException;

public interface FindAuthenticatedStudent {

    Student getAuthenticatedStudent() throws AccessDeniedException;
}
