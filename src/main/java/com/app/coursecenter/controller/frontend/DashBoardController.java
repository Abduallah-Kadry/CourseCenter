package com.app.coursecenter.controller.frontend;

import com.app.coursecenter.entity.User;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("${app.paths.frontend-base}")
public class DashBoardController {


    @GetMapping("/dashboard")
    public String mainControllerDashboard(Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated()) {
            return "redirect:/login";
        }

        String role = authentication.getAuthorities().stream()
                .findFirst()
                .map(GrantedAuthority::getAuthority)
                .toString();

        // Redirect based on role
        switch (role) {
            case "ROLE_ADMIN":
                return "redirect:${app.paths.admin-base}/dashboard";
            case "ROLE_TEACHER":
                return "redirect:${app.paths.teacher-base}/dashboard";
            case "ROLE_STUDENT":
                return "redirect:${app.paths.student-base}/dashboard";
            default:
                return "redirect:/login";
        }

    }

    @GetMapping("${app.paths.admin-base}/dashboard")
    public String adminDashBoard(Authentication authentication, Model model) {
        if (!hasRole(authentication, "ADMIN")) {
            return "redirect:/login";
        }

        User admin = (User) authentication.getPrincipal();

        model.addAttribute("user_details",admin);
        return "admin/dashboard";
    }

    private boolean hasRole(Authentication authentication, String role) {
        if (authentication == null) return false;
        return authentication.getAuthorities()
                .stream().anyMatch(a -> a.getAuthority().equals("ROLE_" + role));
    }

}
