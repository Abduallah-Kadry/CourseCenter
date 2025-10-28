package com.app.coursecenter.util;

import com.app.coursecenter.entity.Student;
import com.app.coursecenter.entity.StudentDetails;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.nio.file.AccessDeniedException;

@Component
public class FindAuthenticatedStudentImpl implements FindAuthenticatedStudent {

    @Override
    public Student getAuthenticatedStudent() throws AccessDeniedException {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        // --- Check if user is authenticated ---
        if (authentication == null || !authentication.isAuthenticated()
                || "anonymousUser".equals(authentication.getPrincipal())) {
            throw new AccessDeniedException("Authentication required");
        }

        Object principal = authentication.getPrincipal();

        // --- Safely unwrap StudentDetails ---
        if (principal instanceof StudentDetails studentDetails) {
            return studentDetails.getStudent();
        }

        // --- Fallback: if someone manually injected Student into context (e.g., during testing) ---
        if (principal instanceof Student student) {
            return student;
        }

        throw new AccessDeniedException("Invalid principal type");
    }
}
