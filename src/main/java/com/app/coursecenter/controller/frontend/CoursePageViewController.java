package com.app.coursecenter.controller.frontend;


import com.app.coursecenter.entity.Course;
import com.app.coursecenter.request.CreateCourseRequest;
import com.app.coursecenter.response.CourseResponse;
import com.app.coursecenter.service.courseservice.CourseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("${app.paths.frontend-base}${app.paths.course-base}") // page
public class CoursePageViewController {
    @Autowired
    CourseService courseService;

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

        return "courses"; // thymeleaf template name (courses.html)
    }

    @GetMapping("/add")
    public String showAddCourseForm(Model model) {
        model.addAttribute("course", new Course());
        return "add-course";
    }

    @GetMapping("/{id}")
    public String showCourseDetail(@PathVariable Long id, Model model) {
        model.addAttribute("course", courseService.getCourseById(id));
        return "course-details";
    }


}
