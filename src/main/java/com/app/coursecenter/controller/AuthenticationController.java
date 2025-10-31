package com.app.coursecenter.controller;

import com.app.coursecenter.exception.AuthenticationException;
import com.app.coursecenter.request.AuthenticationRequest;
import com.app.coursecenter.request.RegisterRequest;
import com.app.coursecenter.response.ApiRespond;
import com.app.coursecenter.response.AuthenticationResponse;
import com.app.coursecenter.service.authenticationservice.AuthenticationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.http.*;
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

        AuthenticationResponse token = authenticationService.login(authenticationRequest);

        ResponseCookie cookie = ResponseCookie.from("jwt", token.getToken())
                .httpOnly(true)
                .secure(false) // change to true if using HTTPS
                .path("/")
                .maxAge(24 * 60 * 60) // 1 day
                .sameSite("Lax")
                .build();


        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, cookie.toString())
                .body(new ApiRespond(HttpStatus.OK, "login successfully",
                        authenticationService.login(authenticationRequest)));

    }

    @Operation(summary = "Logout", description = "Logout user")
    @PostMapping("/logout")
    public ResponseEntity<Void> login(HttpServletResponse response) {

        ResponseCookie cookie = ResponseCookie.from("jwt", "")
                .httpOnly(true)
                .secure(false) // change to true if using HTTPS
                .path("/")
                .maxAge(0) // 1 day
                .sameSite("Lax")
                .build();

        response.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());
        return ResponseEntity.noContent().build();


    }
}
