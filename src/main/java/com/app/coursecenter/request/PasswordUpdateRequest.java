package com.app.coursecenter.request;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


// input requests from the front end should be always validated (or the front browser js may enter unreasonable numbers)

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PasswordUpdateRequest {

    @NotEmpty(message = "Old password is mandatory")
    @Size(min = 5, max = 30, message = "Old password must be at least 5characters long")
    private String oldPassword;

    @NotEmpty(message = "New password is mandatory")
    @Size(min = 5, max = 30, message = "New password must be at least 5characters long")
    private String newPassword;

    @NotEmpty(message = "New password is mandatory")
    @Size(min = 5, max = 30, message = "New password must be at least 5characters long")
    private String confirmPassword;
}
