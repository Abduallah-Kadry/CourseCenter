// Package declaration for exception handling
package com.app.coursecenter.exception.ExceptionsAdvice;

// Importing required Spring Framework classes

import com.app.coursecenter.exception.AuthenticationException;
import com.app.coursecenter.exception.DuplicateResourceException;
import com.app.coursecenter.exception.ResourceNotFoundException;
import com.app.coursecenter.response.ApiRespond;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.server.ResponseStatusException;

import java.util.stream.Collectors;


// Global exception handler for the application
@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<ApiRespond> handleAuthenticationException(AuthenticationException ex) {
        HttpStatus status = HttpStatus.UNAUTHORIZED;
        return buildResponsibility(ex, status);

    }


    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ApiRespond> handleResourceNotFoundException(ResourceNotFoundException ex) {
        HttpStatus status = HttpStatus.NOT_FOUND;
        return buildResponsibility(ex, status);
    }


    @ExceptionHandler(ResponseStatusException.class)
    public ResponseEntity<ApiRespond> handleResponseStatusException(ResponseStatusException ex) {
        return buildResponsibility(ex, HttpStatus.valueOf(ex.getStatusCode().value()));
    }


    @ExceptionHandler(DuplicateResourceException.class)
    public ResponseEntity<ApiRespond> handleDuplicateResourceException(DuplicateResourceException ex) {
        return buildResponsibility(ex, HttpStatus.BAD_REQUEST);
    }


    // field validations handler
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiRespond> handleValidationException(MethodArgumentNotValidException ex) {
        String errorMessage = null;
        errorMessage = ex.getBindingResult().getFieldErrors().stream()
                .map(DefaultMessageSourceResolvable::getDefaultMessage)
                .collect(Collectors.joining(", "));
        return ResponseEntity.badRequest().body(new ApiRespond(HttpStatus.BAD_REQUEST, errorMessage, null));
    }


    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ApiRespond> handleBadJson(HttpMessageNotReadableException ex) {
        return ResponseEntity.badRequest()
                .body(new ApiRespond(HttpStatus.BAD_REQUEST, "Invalid JSON format or data type", ex.getMessage()));
    }


    // Generic exception handler (catch-all)
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiRespond> handleGeneralExceptions(Exception ex) {
        // Default to BAD_REQUEST status for unhandled exceptions
        return buildResponsibility(ex, HttpStatus.INTERNAL_SERVER_ERROR);
    }


    // Helper method to build consistent error responses
    private ResponseEntity<ApiRespond> buildResponsibility(Exception ex, HttpStatus status) {
        return buildResponsibility(ex, ex.getMessage(), status);
    }

    // if you want to put optional messages
    private ResponseEntity<ApiRespond> buildResponsibility(Exception ex, String message, HttpStatus status) {
        ApiRespond error = new ApiRespond(status, message, null);
        return ResponseEntity.status(status).body(error);
    }


}