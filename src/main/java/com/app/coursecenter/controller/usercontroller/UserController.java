package com.app.coursecenter.controller.usercontroller;

import com.app.coursecenter.dto.UserDto;
import com.app.coursecenter.request.PasswordUpdateRequest;
import com.app.coursecenter.response.UserCoursesRespond;
import com.app.coursecenter.service.userservice.UserService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.nio.file.AccessDeniedException;
import java.util.List;

@RestController
@RequestMapping("${app.paths.api-base}/user")
@Tag(name = "User Rest API Endpoints", description = "Operations related to info about current user")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }


    @GetMapping("/enrolledCourses")
    public List<UserCoursesRespond> getEnrolledCourses() {
        return userService.getEnrolledCourses();
    }

    @PostMapping("/rate")
    public ResponseEntity<?> rateCourse(@RequestParam Long courseId, @RequestParam int rate) {
        userService.rateCourse(courseId, rate);
        return ResponseEntity.accepted().body("Rating event sent successfully!");
    }

    @PostMapping("/reserve")
    public ResponseEntity<?> reserveCourse(@RequestParam Long courseId) {
        userService.requestCourseReservation(courseId);
        return ResponseEntity.accepted().body("Reservation command sent");
    }


    @DeleteMapping("/cancel")
    public ResponseEntity<?> cancelReservation(@RequestParam Long courseId) {
        userService.cancelCourseReservation(courseId);
        return ResponseEntity.accepted().body("Cancel command sent");
    }

    // user should see it's profile

    @GetMapping("/info")
    public UserDto getUserInfo() throws AccessDeniedException {
        return userService.getUserInfo();
    }

    // only request for deleting from the admin so this api is for the admin only

    @DeleteMapping
    public void deleteUser() throws AccessDeniedException {
        userService.deleteUser();
    }

    @PutMapping("/password")
    public void passwordUpdate(@Valid @RequestBody PasswordUpdateRequest passwordUpdateRequest) throws AccessDeniedException {
        userService.updatePassword(passwordUpdateRequest);
    }
}
