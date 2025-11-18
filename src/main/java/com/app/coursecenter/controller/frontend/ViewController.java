package com.app.coursecenter.controller.frontend;

import com.app.coursecenter.service.courseservice.CourseService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("${app.paths.frontend-base}") // page
@RequiredArgsConstructor
public class ViewController {

    private final CourseService courseService;

    // todo structure a way to go the home and login page

    @GetMapping("")
    public String home() {
        return "courses";
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping("/register")
    public String register() {
        return "register";
    }

}
