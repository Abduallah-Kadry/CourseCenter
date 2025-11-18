package com.app.coursecenter.controller.coursecontroller;

import com.app.coursecenter.request.CreateCourseRequest;
import com.app.coursecenter.request.UpdateCourseRequest;
import com.app.coursecenter.response.ApiRespond;
import com.app.coursecenter.response.CourseResponse;
import com.app.coursecenter.service.CourseRatingService;
import com.app.coursecenter.service.courseservice.CourseService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

// the student should gets it's enrolled courses
// the student should not register in more than 3 courses

@RestController
@RequestMapping("${app.paths.api-base}${app.paths.course-base}")
@RequiredArgsConstructor
@Tag(name ="Course APIs", description = "Responsible for Course Apis")
public class CourseController {

    private final CourseService courseService;
    private final CourseRatingService courseRatingService;


    // everyone
    @GetMapping("/{id}")
    public ResponseEntity<ApiRespond> getCourseById(@PathVariable long id) {

        return ResponseEntity.ok(new ApiRespond(HttpStatus.OK,
                "course updated successfully", courseService.getCourseById(id)));

    }

    // everyone
    @GetMapping("")
    public ResponseEntity<ApiRespond> getAllCourses(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {

        Page<CourseResponse> courses = courseService.getAllCourses(page, size);
        return ResponseEntity.ok(new ApiRespond(HttpStatus.OK, "All Courses", courses));

    }

    // everyone
    @GetMapping("{courseId}/average-rate")
    public ResponseEntity<Double> getAverageRate(@PathVariable Long courseId) {
        Double averageRate = courseRatingService.getAverageCourseRate(courseId);
        return ResponseEntity.ok(averageRate);
    }

    // admin
    @PostMapping("")
    public ResponseEntity<ApiRespond> createCourse(@Valid @ModelAttribute CreateCourseRequest course) {

        return ResponseEntity.ok(new ApiRespond(HttpStatus.OK,
                "course added successfully", courseService.addCourse(course)));

    }

    // admin
    @PutMapping("/{id}")
    public ResponseEntity<ApiRespond> updateCourse(@PathVariable long id, @Valid @ModelAttribute UpdateCourseRequest course) {
        return ResponseEntity.ok(new ApiRespond(HttpStatus.OK,
                "course updated successfully", courseService.updateCourse(id, course)));
    }


    // admin
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiRespond> deleteCourse(@PathVariable long id) {
        return ResponseEntity.ok(new ApiRespond(HttpStatus.OK,
                "course updated successfully", courseService.deleteCourse(id)));
    }


}
