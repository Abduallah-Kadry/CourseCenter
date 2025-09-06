package com.app.coursecenter.util;

import com.app.coursecenter.entity.Student;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.nio.file.AccessDeniedException;

@Component
public class FindAuthenticatedStudentImpl implements FindAuthenticatedStudent {


    @Override
    public Student getAuthenticatedStudent() throws AccessDeniedException {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication != null || !authentication.isAuthenticated()
                || authentication.getPrincipal().equals("anonymousUser")) {

            throw new AccessDeniedException("Authentication required");
        }

        return (Student) authentication.getPrincipal();
    }
}
