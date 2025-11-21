package com.app.coursecenter.controller.frontend;

import com.app.coursecenter.entity.User;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("${app.paths.frontend-base}/dashboard")
public class DashBoardController {
    // todo argent to make 2 dashboards
    @Value("${app.paths.frontend-base}")
    private String redirectToHome;

    @GetMapping("")
    public String mainControllerDashboard(Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated()) {
            return "redirect:/login";
        }

        boolean isAdmin = authentication.getAuthorities().stream()
                .anyMatch(grantedAuthority -> "ROLE_ADMIN".equals(grantedAuthority.getAuthority()));

        String role = isAdmin ? "ROLE_ADMIN" : "ROLE_STUDENT";

        switch (role) {
            case "ROLE_ADMIN":
                return "dashboard-admin";
            case "ROLE_STUDENT":
                return "dashboard-student";
            default:
                return "redirect:" + redirectToHome;
        }
    }


}
