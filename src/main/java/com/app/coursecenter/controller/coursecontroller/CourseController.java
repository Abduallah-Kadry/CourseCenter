package com.app.coursecenter.controller.coursecontroller;

import com.app.coursecenter.entity.Course;
import com.app.coursecenter.request.CreateCourseRequest;
import com.app.coursecenter.service.courseservice.CourseService;
import lombok.RequiredArgsConstructor;
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
    public ResponseEntity<Page<CourseDto>> getAllCourses(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        try {
                Page<CourseDto> courses = courseService.getAllCourses(page, size);
            return ResponseEntity.ok(new ApiRespond(HttpStatus.OK, "All Courses", courses));
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

    @GetMapping("/id")
    public ResponseEntity<CourseDto> getCourseById(@RequestParam("id") long id) {
        try {
            //
            return ResponseEntity.ok(courseService.getCourseById(id));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

}
