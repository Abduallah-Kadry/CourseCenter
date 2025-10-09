package com.app.coursecenter.exception;

public class InvalidCredentialException extends AuthenticationException{
    public InvalidCredentialException() {
        this("Invalid Credentials");
    }

    public InvalidCredentialException(String message) {
        super(message);
    }
}
