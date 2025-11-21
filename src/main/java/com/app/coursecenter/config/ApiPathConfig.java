package com.app.coursecenter.config;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

@Getter
@Setter
@Configuration
@ConfigurationProperties(prefix = "app.paths")
public class ApiPathConfig {

    /* todo
    need a practical way to group apis by
    1. role (admin, student, teacher, etc)
    2. http method
    3. front end for getting the pages with proper info (like update user or course)
    4. backend for functions (api/something**
    5. schema like course, user, subscription (api/course, api/user, api/subscription)
    6. easy integration with security and usage across the app in controllers and variables
    7. it should support easy add and update apis
     */

    /* *

        - for this to be done what i have here is the group of public apis and for each like schema have both front and backend
        but it lacks the http method where i think i need to remake the grouping so that i can easily add what api i want
        and with it's http method
        - second point is scalability where i need to think of how i firmly construct my api architecture so that
        they don't go into each other way and easily group them in a way that i apply some security and routing logic on
        them

    */

    /* *
    ok paths tha i use for now

    front end viewing
    /page
        /login ,/register ,/course ,/add-course ,/update/{id}, /dashboard ,/profile   <= these should be role based


    Api
    /api
        /login ,/register
        /course
            /add
            /update/{id}
            /delete/{id}
        /user
            /rate course => student only ... when it rates ... it should take credintials and rate ... so it should be in the coruse
            /

     */

    /* ?
        latest thoughts 2025/nov/16
        you may start with some base in the api like course, user, page for front end
        then to combine these bases and control the http method and role just use a custom static class like ApiPermission

        then provide a getter per controller where you provide list of api
     */


    /* !
        need a way to automatically register each controller api method and role in the security config
    */

    // Base paths => typically for api use not view page uses
    // view pages will be one time and won't have much versioning

    private String apiBase = "/api";
    private String frontendBase = "/page";
    private String userBase = "/user";
    private String enrollmentBase = "/enrollment";


    // Public endpoints (no auth required)
    private String[] publicPaths = {
            "/", // welcome page
            "/js/**",
            "/css/**",
            "/swagger-ui/**",
            "/v3/api-docs/**",
            "/swagger-resources/**",
            "/uploads/**",
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

    // login and register apis
    private String[] authEndpoints = {"/register", "/login"};


    // Role-based paths (useless for now)
    String[] roleAdmin = {"ROLE_ADMIN"};
    String[] roleStudent = {"ROLE_STUDENT"};
    String[] roleAdminAndStudent = {"ROLE_STUDENT", "ROLE_ADMIN"};

    // Course-related paths
    private String courseBase = "/course";

    // user-course


    private String[] adminFrontEndCoursePaths = {"/add", "/update/**"};
    private String[] adminApiCoursePaths = {"", "/update/**"};
    private String[] publicCourseFrontPaths = {"", "/{id}"};
    private String[] publicCourseApiPaths = {"", "/{id}", "/{id}/average-rate"};

    @Data
    @AllArgsConstructor
    public static class ApiPermission {
        private String path;
        private HttpMethod method;
        private String[] roles;
    }

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


    public String getFrontendPath() {
        return frontendBase + "/**";
    }

    // Helper methods for course paths
    public String[] getAdminCourseFrontendPaths() {
        return Stream.of(adminFrontEndCoursePaths)
                .map(path -> frontendBase + courseBase + path)
                .toArray(String[]::new);
    }

    public List<ApiPermission> getAdminCourseApiPaths() {
        return List.of(
                new ApiPermission(apiBase + courseBase, HttpMethod.POST, roleAdmin),
                new ApiPermission(apiBase + courseBase, HttpMethod.DELETE, roleAdmin),
                new ApiPermission(apiBase + courseBase + "/{id}", HttpMethod.PUT, roleAdmin)

        );
    }

    public List<ApiPermission> getUserControllerApi() {

        return List.of(
                new ApiPermission(apiBase + userBase + "/enrolledCourses", HttpMethod.GET, roleAdminAndStudent),
                new ApiPermission(apiBase + userBase + "/password", HttpMethod.PUT, roleAdminAndStudent),
                new ApiPermission(apiBase + userBase + "/info", HttpMethod.GET, roleAdminAndStudent),
                new ApiPermission(apiBase + userBase, HttpMethod.DELETE, roleAdmin),
                new ApiPermission(apiBase + userBase + courseBase + "/rate", HttpMethod.POST, roleStudent),
                new ApiPermission(apiBase + userBase + courseBase + "/reserve/{courseId}", HttpMethod.POST, roleStudent),
                new ApiPermission(apiBase + userBase + courseBase + "/cancel", HttpMethod.DELETE, roleStudent)
        );

    }


    public String[] getPublicCourseFrontPaths() {
        return Stream.of(publicCourseFrontPaths)
                .map(path -> frontendBase + courseBase + path)
                .toArray(String[]::new);
    }

    public String[] getPublicCourseApiPaths() {
        return Stream.of(publicCourseApiPaths)
                .map(path -> apiBase + courseBase + path)
                .toArray(String[]::new);
    }


    // build the full public paths (need improvement)
    public String[] getAllPublicPaths() {
        return Stream.concat(
                Stream.concat(
                        Stream.concat(
                                Arrays.stream(publicPaths),
                                Arrays.stream(getPublicApiPaths())
                        ),
                        Arrays.stream(getPublicCourseFrontPaths())
                ),
                Arrays.stream(getPublicCourseApiPaths()) // NEW: Include public course API paths
        ).toArray(String[]::new);
    }

    public String getHomePage() {
        return frontendBase + courseBase;
    }
}