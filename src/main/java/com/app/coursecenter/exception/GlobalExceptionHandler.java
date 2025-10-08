// Package declaration for exception handling
package com.app.coursecenter.exception;

// Importing required Spring Framework classes

import com.app.coursecenter.response.ApiRespond;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.server.ResponseStatusException;


// Global exception handler for the application
@ControllerAdvice
public class GlobalExceptionHandler {


    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<ApiRespond> handleAuthenticationException(AuthenticationException ex) {
        HttpStatus status = HttpStatus.UNAUTHORIZED;
        // Delegate to helper with appropriate status
        return ResponseEntity.status(status)
                .body(new ApiRespond(status,ex.getMessage(),null));
    }


    // Helper method to build consistent error responses
    private ResponseEntity<ApiRespond> buildResponsibility(Exception ex, HttpStatus status) {
        // Create new error response object
        ApiRespond error = new ApiRespond();

        // Set response status code
        error.setStatus(status.value());
        // Set error message from exception
        error.setMessage(ex.getMessage());
        // Set current timestamp
        error.setTimestamp(System.currentTimeMillis());

        // Return response entity with error and status
        return new ResponseEntity<>(error, status);
    }


    // Handle ResponseStatusException specifically
    @ExceptionHandler(ResponseStatusException.class)
    public ResponseEntity<ApiRespond> handleException(ResponseStatusException ex) {
        // Delegate to helper with appropriate status
        return buildResponsibility(ex, HttpStatus.valueOf(ex.getStatusCode().value()));
    }


    // Generic exception handler (catch-all)
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiRespond> handleException(Exception ex) {
        // Default to BAD_REQUEST status for unhandled exceptions
        return buildResponsibility(ex, HttpStatus.BAD_REQUEST);
    }
}