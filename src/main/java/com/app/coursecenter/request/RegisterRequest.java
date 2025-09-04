package com.app.coursecenter.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class RegisterRequest {

    @NotEmpty(message = "First name is mandatory")
    @Size(min = 1, max = 30, message = "First name must be at least 3 characters long")
    private String firstName;

    @NotEmpty(message = "Last name is mandatory")
    @Size(min = 1, max = 30, message = "Last name must be at least 3 characters long")
    private String lastName;

    @NotEmpty(message = "Email is mandatory")
    @Email
    private String email;

    @NotEmpty(message = "Password is mandatory")
    @Size(min = 5, max = 30, message = "Password must be at least 5 characters long")
    private String password;
}
