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
    public String courses(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            Model model) {

        // Fetch paginated courses from the service
        Page<CourseResponse> coursePage = courseService.getAllCourses(page, size);

        // Add attributes to the model
        model.addAttribute("courses", coursePage.getContent()); // list of courses
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", coursePage.getTotalPages());
        model.addAttribute("totalElements", coursePage.getTotalElements());
        model.addAttribute("pageSize", size);

        return "courses"; // thymeleaf template name (courses.html)
    }
}
