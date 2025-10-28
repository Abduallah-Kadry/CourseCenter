package com.app.coursecenter.controller.coursecontroller;

import com.app.coursecenter.entity.Course;
import com.app.coursecenter.request.CreateCourseRequest;
import com.app.coursecenter.response.ApiRespond;
import com.app.coursecenter.service.courseservice.CourseService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/course")
@RequiredArgsConstructor
// the student should gets it's enrolled courses
// the stuednt should request to be added to the course
// the student should not register in more than 3 courses
public class CourseController {

    private final CourseService courseService;


    @PostMapping("")
    public ResponseEntity<Course> createCourse(@RequestBody CreateCourseRequest course) {
        try {
            //
            return ResponseEntity.ok(courseService.addCourse(course));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping("")
    public ResponseEntity<ApiRespond> getAllCourses() {
        try {
            return ResponseEntity.ok(new ApiRespond(HttpStatus.OK, "All Courses", courseService.getAllCourses()));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

//    @GetMapping("/{id}")
//    public ResponseEntity<Set<Course>> getCoursesByStudentId(@PathVariable("id") Long studentId) {
//        try {
//            //
//            return ResponseEntity.ok(courseService.getCoursesByStudentId(studentId));
//        } catch (Exception e) {
//            return ResponseEntity.internalServerError().build();
//        }
//    }
}
