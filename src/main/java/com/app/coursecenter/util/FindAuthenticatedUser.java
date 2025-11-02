package com.app.coursecenter.util;

import com.app.coursecenter.entity.User;

import java.nio.file.AccessDeniedException;

public interface FindAuthenticatedUser {

    User getAuthenticatedUser() throws AccessDeniedException;
}
