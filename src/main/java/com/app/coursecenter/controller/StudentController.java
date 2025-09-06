package com.app.coursecenter.controller;

import com.app.coursecenter.dto.StudentDto;
import com.app.coursecenter.request.PasswordUpdateRequest;
import com.app.coursecenter.service.StudentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.nio.file.AccessDeniedException;

@RestController
@RequestMapping("/api/students")
@Tag(name = "Student Rest API Endpoints", description = "Operations related to info about current student")
public class StudentController {

    private final StudentService studentService;

    public StudentController(StudentService studentService) {
        this.studentService = studentService;
    }

    @Operation(summary = "User Information", description = "Get Current User Info")
    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/info")
    public StudentDto getStudentInfo() throws AccessDeniedException {
        return studentService.getStudentInfo();
    }

    @Operation(summary = "Delete User ", description = "Delete Current User Account")
    @ResponseStatus(HttpStatus.OK)
    @DeleteMapping
    public void deleteStudent() throws AccessDeniedException {
        studentService.deleteStudent();
    }

    @Operation(summary = "Password Update", description = "Change User Password After Verification")
    @ResponseStatus(HttpStatus.OK)
    @PutMapping("/password")
    public void passwordUpdate(@Valid @RequestBody PasswordUpdateRequest passwordUpdateRequest) throws AccessDeniedException {
        studentService.updatePassword(passwordUpdateRequest);
    }
}
