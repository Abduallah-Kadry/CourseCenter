package com.app.coursecenter.controller.StudentController;


import com.app.coursecenter.Service.courseservice.CourseService;
import com.app.coursecenter.Service.courseservice.studentservice.StudentService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/student")
@RequiredArgsConstructor
public class StudentController {
    private final StudentService studentService;

}
