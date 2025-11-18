package com.app.coursecenter.controller.frontend;


import com.app.coursecenter.entity.Course;
import com.app.coursecenter.response.CourseResponse;
import com.app.coursecenter.service.courseservice.CourseService;
import com.app.coursecenter.service.userservice.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
@RequestMapping("${app.paths.frontend-base}${app.paths.course-base}") // page
public class CoursePageViewController {

    private final CourseService courseService;
    private final UserService userService;


    @GetMapping("")
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

        return "courses";
    }

    @GetMapping("/add")
    public String showAddCourseForm(Model model) {
        model.addAttribute("course", new Course());
        return "add-course";
    }

    @GetMapping("/update/{id}")
    public String showCourseUpdateForm(Model model, @PathVariable Long id) {
        model.addAttribute("course", courseService.getCourseById(id));
        return "update-course";
    }

    @GetMapping("/{id}")
    public String showCourseDetail(@PathVariable Long id, Model model) {
        System.out.println("iam here");
        System.out.println(userService.getEnrolledCourses().stream().anyMatch(s -> s.getId().equals(id)));
        System.out.println("iam here");

        model.addAttribute("isEnrolled", userService.getEnrolledCourses().stream().anyMatch(s -> s.getId().equals(id)));

        model.addAttribute("course", courseService.getCourseById(id));
        return "course-details";
    }

}
