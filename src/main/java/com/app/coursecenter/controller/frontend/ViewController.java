package com.app.coursecenter.controller.frontend;

import com.app.coursecenter.response.CourseResponse;
import com.app.coursecenter.service.courseservice.CourseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("${app.paths.frontend-base}") // page
public class ViewController {

    @Autowired
    CourseService courseService;

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

    // Add more view mappings as needed




}
