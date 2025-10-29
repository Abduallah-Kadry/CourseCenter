package com.app.coursecenter.controller.frontend;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Controller
@RequestMapping("${app.paths.frontend-base}")
public class ViewController {

    // todo structure a way to go the home and login page

    @GetMapping("")
    public String home() {
        return "index";
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

    @GetMapping("/courses")
    public String courses() {
        return "courses";
    }
}
