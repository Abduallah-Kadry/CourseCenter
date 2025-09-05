package com.app.coursecenter.controller.coursecontroller;

import com.app.coursecenter.entity.Course;
import com.app.coursecenter.service.courseservice.CourseService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/course")
@RequiredArgsConstructor

public class CourseController {

    private final CourseService courseService;

    @GetMapping("")
    public ResponseEntity<List<Course>> getAllCourses() {
        try {
            return ResponseEntity.ok(courseService.getAllCourses());
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }

    }
}
