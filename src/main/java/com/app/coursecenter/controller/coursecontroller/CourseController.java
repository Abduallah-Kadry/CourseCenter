package com.app.coursecenter.controller.coursecontroller;

import com.app.coursecenter.entity.Course;
import com.app.coursecenter.request.CreateCourseRequest;
import com.app.coursecenter.request.UpdateCourseRequest;
import com.app.coursecenter.response.ApiRespond;
import com.app.coursecenter.service.courseservice.CourseService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.parameters.P;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;


// the student should gets it's enrolled courses
// the stuednt should request to be added to the course
// the student should not register in more than 3 courses


@RestController
@RequestMapping("/course")
@RequiredArgsConstructor
@Tag(name = "Course Rest API End Points", description = "Api Rest Points related to the Course CRUD")
public class CourseController {

    private final CourseService courseService;

    @GetMapping("")
    public ResponseEntity<ApiRespond> getAllCourses() {
        return ResponseEntity.ok(new ApiRespond(HttpStatus.OK, "Courses fetched successfully",
                courseService.getAllCourses()));
    }

    @GetMapping("/{courseId})")
    public ResponseEntity<ApiRespond> getCourseById(@PathVariable("courseId") Long courseId) {
        return ResponseEntity.ok(new ApiRespond(HttpStatus.OK, "Course fetched successfully",
                courseService.getCourseById(courseId)));
    }

    @GetMapping("/student/{studentId}")
    public ResponseEntity<ApiRespond> getCoursesByStudentId(@PathVariable("studentId") Long studentId) {
        return ResponseEntity.ok(new ApiRespond(HttpStatus.OK, "Courses for student fetched successfully",
                courseService.getCoursesByStudentId(studentId)));
    }

    @PostMapping("")
    public ResponseEntity<ApiRespond> createCourse(@RequestBody CreateCourseRequest course) {
        return ResponseEntity.ok(new ApiRespond(HttpStatus.OK, "Course created successfully",
                courseService.addCourse(course)));
    }


    @PutMapping("/{courseId}")
    public ResponseEntity<ApiRespond> updateCourse(@PathVariable("courseId") Long courseId, UpdateCourseRequest course) {
        return ResponseEntity.ok(new ApiRespond(HttpStatus.OK, "Course Updated successfully",
                courseService.updateCourse(courseId, course)));
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<ApiRespond> deleteCourseId(@PathVariable("id") Long courseId) {

        return ResponseEntity.ok(new ApiRespond(HttpStatus.OK, "Course deleted successfully",
                courseService.deleteCourse(courseId)));
    }


}
