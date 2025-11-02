package com.app.coursecenter.util;

import com.app.coursecenter.entity.User;
import com.app.coursecenter.entity.UserCredintials;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.nio.file.AccessDeniedException;

@Component
public class FindAuthenticatedUserImpl implements FindAuthenticatedUser {

    @Override
    public User getAuthenticatedUser() throws AccessDeniedException {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        // --- Check if user is authenticated ---
        if (authentication == null || !authentication.isAuthenticated()
                || "anonymousUser".equals(authentication.getPrincipal())) {
            throw new AccessDeniedException("Authentication required");
        }

        Object principal = authentication.getPrincipal();

        // --- Safely unwrap StudentDetails ---
        if (principal instanceof UserCredintials userCredintials) {
            return userCredintials.getUser();
        }

        // --- Fallback: if someone manually injected Student into context (e.g., during testing) ---
        if (principal instanceof User user) {
            return user;
        }

        throw new AccessDeniedException("Invalid principal type");
    }
}
