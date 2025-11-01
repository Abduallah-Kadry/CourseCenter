package com.app.coursecenter.exception;

public class DuplicateResourceException extends RuntimeException{
    public DuplicateResourceException() {
        this("Already Exists!");
    }

    public DuplicateResourceException(String message) {
        super(message);
    }
}
