package com.app.coursecenter.exception.ExceptionsAdvice;

import com.app.coursecenter.response.ApiRespond;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class SecurityExceptionsHandler {

    @ExceptionHandler(io.jsonwebtoken.ExpiredJwtException.class)
    public ResponseEntity<ApiRespond> JwtTokenExpiredExceptionHandler(Exception ex){
        return buildResponsibility(ex,"Session Expired Please Login!",HttpStatus.UNAUTHORIZED);
    }

      // Helper method to build consistent error responses
    private ResponseEntity<ApiRespond> buildResponsibility(Exception ex, HttpStatus status) {
        return buildResponsibility(ex, ex.getMessage(), status);
    }

    // if you want to put optional messages
    private ResponseEntity<ApiRespond> buildResponsibility(Exception ex, String message, HttpStatus status) {
        ApiRespond error = new ApiRespond(status, message, null);
        return ResponseEntity.ofNullable(error);
    }

}
