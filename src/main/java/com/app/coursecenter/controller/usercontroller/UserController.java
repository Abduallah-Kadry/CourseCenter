package com.app.coursecenter.controller.usercontroller;

import com.app.coursecenter.dto.UserDto;
import com.app.coursecenter.request.PasswordUpdateRequest;
import com.app.coursecenter.response.ApiRespond;
import com.app.coursecenter.response.CourseResponse;
import com.app.coursecenter.response.UserCoursesRespond;
import com.app.coursecenter.service.userservice.UserService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.nio.file.AccessDeniedException;
import java.util.List;

@RestController
@RequestMapping("${app.paths.api-base}${app.paths.user-base}")
@Tag(name = "User Rest API Endpoints", description = "Operations related to info about current user")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }


    // admin and student
    @GetMapping("/enrolledCourses")
    public List<CourseResponse> getEnrolledCourses() {
        return userService.getEnrolledCourses();
    }

     // admin and student
    @PutMapping("/password")
    public void passwordUpdate(@Valid @RequestBody PasswordUpdateRequest passwordUpdateRequest) throws AccessDeniedException {
        userService.updatePassword(passwordUpdateRequest);
    }
     // admin and student
    @GetMapping("/info")
    public UserDto getUserInfo() throws AccessDeniedException {
        return userService.getUserInfo();
    }

     // admin
    @DeleteMapping
    public void deleteUser() throws AccessDeniedException {
        userService.deleteUser();
    }

    // student
    @PostMapping("/${app.paths.course-base}/rate")
    public ResponseEntity<?> rateCourse(@RequestParam Long courseId, @RequestParam int rate) {
        userService.rateCourse(courseId, rate);
        return ResponseEntity.accepted().body("Rating event sent successfully!");
    }

    // student
    @PreAuthorize("ROLE_STUDENT")
    @PostMapping("${app.paths.course-base}/reserve/{courseId}")
    public ResponseEntity<ApiRespond> reserveCourse(@PathVariable Long courseId) {
        userService.requestCourseReservation(courseId);
        return ResponseEntity.accepted().body(new ApiRespond(HttpStatus.OK,null,"Reservation command sent"));
    }

    // student
    @DeleteMapping("/${app.paths.course-base}/cancel")
    public ResponseEntity<?> cancelReservation(@RequestParam Long courseId) {
        userService.cancelCourseReservation(courseId);
        return ResponseEntity.accepted().body("Cancel command sent");
    }


    // only request for deleting from the admin so this api is for the admin only





}
