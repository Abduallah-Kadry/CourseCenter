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
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/course")
@RequiredArgsConstructor
// the student should gets it's enrolled courses
// the stuednt should request to be added to the course
// the student should not register in more than 3 courses
@Tag(name = "Course Rest API Endpoints", description = "Operations related courses")
public class CourseController {

    private final CourseService courseService;


    @PostMapping("")
    public ResponseEntity<ApiRespond> createCourse(@RequestBody CreateCourseRequest course) {
        try {
            return ResponseEntity.ok(new ApiRespond(HttpStatus.OK,
                    "course added successfully", courseService.addCourse(course)));
        } catch (Exception e) {
          return ResponseEntity.ok(new ApiRespond(HttpStatus.OK,
                    "Problem occurred", e.getMessage()));
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

    @PutMapping("/{id}")
    public ResponseEntity<ApiRespond> updateCourse(@PathVariable long id, @RequestBody UpdateCourseRequest course) {
        try {
            return ResponseEntity.ok(new ApiRespond(HttpStatus.OK,
                    "course updated successfully", courseService.updateCourse(id, course)));
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
