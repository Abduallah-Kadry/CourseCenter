package com.app.coursecenter.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Getter;

@Getter
public class AuthenticationRequest {

    @NotEmpty(message = "Email is mandatory")
    @Email(message = "Invalid Email Format")
    private String email;

    @NotEmpty(message = "Password is mandatory")
    @Size(min = 5, max = 30, message = "Password must be at least 5 characters long")
    private String password;
}
