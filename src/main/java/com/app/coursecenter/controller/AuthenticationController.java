package com.app.coursecenter.controller;

import com.app.coursecenter.exception.AuthenticationException;
import com.app.coursecenter.request.AuthenticationRequest;
import com.app.coursecenter.request.RegisterRequest;
import com.app.coursecenter.response.ApiRespond;
import com.app.coursecenter.service.authenticationservice.AuthenticationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("${app.paths.api-base}${app.paths.auth-base}")
@Tag(name = "Authentication REST API Endpoints", description = "Operations related to register & login")
public class AuthenticationController {

    private final AuthenticationService authenticationService;

    public AuthenticationController(AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }

    @Operation(summary = "Register a Student", description = "register (add) a new Student")
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/register")
    public void register(@Valid @RequestBody RegisterRequest registerRequest) throws Exception {
        authenticationService.register(registerRequest);
    }


    @Operation(summary = "Login a User", description = "Submit email & password to authenticate a student")
    @ResponseStatus(HttpStatus.OK)
    @PostMapping("/login")
    public ResponseEntity<ApiRespond> login(@Valid @RequestBody AuthenticationRequest authenticationRequest) throws AuthenticationException {
        return ResponseEntity.ok(new ApiRespond(HttpStatus.OK, "login successfully", authenticationService.login(authenticationRequest)));

    }
}
