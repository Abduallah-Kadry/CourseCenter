package com.app.coursecenter.config;


import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.Arrays;
import java.util.stream.Stream;

@Getter
@Setter
@Configuration
@ConfigurationProperties(prefix = "app.paths")
public class ApiPathConfig {

    // Base paths
    private String apiBase = "/api";
    private String frontendBase = "/page";

    // Public endpoints (no auth required)
    private String[] publicPaths = {
            "/", // welcome page
            "/swagger-ui/**",
            "/v3/api-docs/**",
            "/swagger-resources/**",
            "/webjars/**",
            "/docs",
            "/error"
    };

    // frontend end points
    private String[] frontEndPublicPages = {
            "/courses",
            "/login",
            "/register",
            "/index",
            "/forgot-password",
            "/reset-password",

    };
    private String[] frontEndAuthPages = {"/profile", "/setting", "/dashboard"};


    // Auth endpoints
    private String authBase = "/auth";
    private String[] authEndpoints = {"/register", "/login"};


    // Role-based paths
    private String adminBase = "/admin";
    private String studentBase = "/student";
    private String teacherBase = "/teacher";


    // Helper methods to build full paths
    public String[] getPublicApiPaths() {
        return Stream.of(authEndpoints)
                .map(endpoint -> apiBase + authBase + endpoint)
                .toArray(String[]::new); // this gets the full paths of endpoints
    }

    public String[] getAuthFrontEndPaths() {
        return Stream.of(frontEndAuthPages)
                .map(endpoint -> frontendBase + endpoint)
                .toArray(String[]::new);
    }

    public String[] getPublicFrontEndPaths() {
        return Stream.of(frontEndPublicPages)
                .map(endpoint -> frontendBase + endpoint)
                .toArray(String[]::new);
    }

    public String getAdminApiPath() {
        return apiBase + adminBase + "/**";
    }

    public String getStudentApiPath() {
        return apiBase + studentBase + "/**";
    }

    public String getTeacherApiPath() {
        return apiBase + teacherBase + "/**";
    }

    public String getFrontendPath() {
        return frontendBase + "/**";
    }


    public String[] getAllPublicPaths() {
        return Stream.concat(
                Arrays.stream(publicPaths),
                Arrays.stream(getPublicApiPaths())
        ).toArray(String[]::new);
    }

}
