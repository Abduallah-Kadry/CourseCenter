package com.app.coursecenter.controller.coursecontroller;

import com.app.coursecenter.dto.CourseDto;
import com.app.coursecenter.request.CreateCourseRequest;
import com.app.coursecenter.request.UpdateCourseRequest;
import com.app.coursecenter.response.ApiRespond;
import com.app.coursecenter.response.CourseResponse;
import com.app.coursecenter.service.courseservice.CourseService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/course")
@RequiredArgsConstructor
// the student should gets it's enrolled courses
// the stuednt should request to be added to the course
// the student should not register in more than 3 courses


public class CourseController {

    private final CourseService courseService;

    @PostMapping("")
    public ResponseEntity<ApiRespond> createCourse(@Valid @RequestBody CreateCourseRequest course) {
        return ResponseEntity.ok(new ApiRespond(HttpStatus.OK,
                "course added successfully", courseService.addCourse(course)));

    }

    @GetMapping("")
    public ResponseEntity<ApiRespond> getAllCourses(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {

        Page<CourseResponse> courses = courseService.getAllCourses(page, size);
        return ResponseEntity.ok(new ApiRespond(HttpStatus.OK, "All Courses", courses));

    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiRespond> updateCourse(@PathVariable long id, @RequestBody UpdateCourseRequest course) {
        return ResponseEntity.ok(new ApiRespond(HttpStatus.OK,
                "course updated successfully", courseService.updateCourse(id, course)));
    }

    @GetMapping("/id")
    public ResponseEntity<CourseResponse> getCourseById(@RequestParam("id") long id) {
        try {
            //
            return ResponseEntity.ok(courseService.getCourseById(id));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

}
