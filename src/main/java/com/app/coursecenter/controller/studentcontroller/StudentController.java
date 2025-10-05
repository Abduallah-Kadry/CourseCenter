package com.app.coursecenter.controller.studentcontroller;

import com.app.coursecenter.dto.StudentDto;
import com.app.coursecenter.request.PasswordUpdateRequest;
import com.app.coursecenter.service.studentservice.StudentService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
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


    // student should see it's profile

    @GetMapping("/info")
    public StudentDto getStudentInfo() throws AccessDeniedException {
        return studentService.getStudentInfo();
    }

    // only request for deleting from the admin so this api is for the admin only

    @DeleteMapping
    public void deleteStudent() throws AccessDeniedException {
        studentService.deleteStudent();
    }

    @PutMapping("/password")
    public void passwordUpdate(@Valid @RequestBody PasswordUpdateRequest passwordUpdateRequest) throws AccessDeniedException {
        studentService.updatePassword(passwordUpdateRequest);
    }
}
