package com.app.coursecenter.controller.coursecontroller.enrollmentController;

import com.app.coursecenter.response.ApiRespond;
import com.app.coursecenter.response.CourseResponse;
import com.app.coursecenter.service.enrollmentService.ReservationService;
import com.app.coursecenter.service.userservice.UserService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;


// todo change each "reserve" word to "enroll" word

@RestController
@RequestMapping("${app.paths.api-base}${app.paths.enrollment-base}")
@Tag(name = "User Rest API Endpoints", description = "Operations related to info about current user")
public class EnrollmentController {

    private final UserService userService;
    private final ReservationService reservationService;

    public EnrollmentController(UserService userService, ReservationService reservationService) {
        this.userService = userService;
        this.reservationService = reservationService;
    }

    // admin and student
    @PreAuthorize("hasAnyRole('ROLE_STUDENT', 'ROLE_ADMIN')")
    @GetMapping("/enrolledCourses")
    public ResponseEntity<ApiRespond> getEnrolledCourses() {

        return ResponseEntity.ok().body(new ApiRespond(HttpStatus.OK, "Enrolled Courses",
                        reservationService.getEnrolledCourses()));
    }


    // student
    @PostMapping("${app.paths.course-base}/rate")
    public ResponseEntity<?> rateCourse(@RequestParam Long courseId, @RequestParam int rate) {
        userService.rateCourse(courseId, rate);
        return ResponseEntity.accepted().body("Rating event sent successfully!");
    }

    @GetMapping("/non-enrolled-random/{count}")
    public ResponseEntity<ApiRespond> getNonEnrolledCourses(@PathVariable int count) {
        return ResponseEntity.ok().body(
                new ApiRespond(HttpStatus.OK, "Random %d non enrolled courses".formatted(count),
                        reservationService.getRandomNonEnrolledCourses(count)));
    }

    // student
    @PreAuthorize("ROLE_STUDENT")
    @PostMapping("/reserve/{courseId}")
    public ResponseEntity<ApiRespond> reserveCourse(@PathVariable Long courseId) {
        reservationService.requestCourseReservation(courseId);
        return ResponseEntity.accepted().body(new ApiRespond(HttpStatus.OK, null, "Reservation command sent"));
    }

    // student
    @PreAuthorize("ROLE_STUDENT")

    @DeleteMapping("${app.paths.course-base}/cancel")
    public ResponseEntity<?> cancelReservation(@RequestParam Long courseId) {
        reservationService.cancelCourseReservation(courseId);
        return ResponseEntity.accepted().body("Cancel command sent");
    }
}
