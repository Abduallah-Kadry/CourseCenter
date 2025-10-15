package com.app.coursecenter.util;

import com.app.coursecenter.entity.Student;
import org.springframework.security.access.AccessDeniedException;


public interface FindAuthenticatedStudent {

    Student getAuthenticatedStudent() throws AccessDeniedException;
}
