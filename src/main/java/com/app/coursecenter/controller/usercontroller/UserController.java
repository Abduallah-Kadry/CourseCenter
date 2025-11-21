package com.app.coursecenter.controller.usercontroller;

import com.app.coursecenter.request.PasswordUpdateRequest;
import com.app.coursecenter.response.ApiRespond;
import com.app.coursecenter.response.CourseResponse;
import com.app.coursecenter.response.UserDetailResponse;
import com.app.coursecenter.service.userservice.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import org.springframework.data.domain.Page;
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

    @Operation(summary = "Get all user on pages default page size 5", description = "Retrieve a list of all students in the system")
    @ResponseStatus(HttpStatus.OK)
    @GetMapping("")
    public ResponseEntity<ApiRespond> getAllUsers(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        Page<UserDetailResponse> users = userService.getAllUser(page, size);
        users.forEach(System.out::println);

        return ResponseEntity.ok().body(new ApiRespond(HttpStatus.OK, "UserLists", users));
    }

    // admin and student
    @PutMapping("/password")
    public void passwordUpdate(@Valid @RequestBody PasswordUpdateRequest passwordUpdateRequest) throws AccessDeniedException {
        userService.updatePassword(passwordUpdateRequest);
    }


    // admin and student
    @GetMapping("/info")
    public UserDetailResponse getUserInfo() throws AccessDeniedException {
        return userService.getUserInfo();
    }

    // admin
    @DeleteMapping
    public void deleteUser() throws AccessDeniedException {
        userService.deleteUser();
    }

    // useless
    @Operation(summary = "Promote user to admin", description = "Promote user to admin role")
    @ResponseStatus(HttpStatus.OK)
    @PutMapping("/{userId}/role")
    public UserDetailResponse promoteToAdmin(@PathVariable @Min(1) long userId) {
        return userService.promoteToAdmin(userId);
    }

    @Operation(summary = "Delete user", description = "Delete a non-admin user from the system")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{userId}")
    public void deleteUser(@PathVariable @Min(1) long userId) {
        userService.deleteNonAdminUser(userId);
    }


}
