package com.app.coursecenter.config.securityconfig;

import com.app.coursecenter.config.ApiPathConfig;
import com.app.coursecenter.entity.UserCredintials;
import com.app.coursecenter.repository.UserRepository;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final UserRepository userRepository;
    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private final ApiPathConfig pathConfig;

    public SecurityConfig(UserRepository userRepository, JwtAuthenticationFilter jwtAuthenticationFilter, ApiPathConfig pathConfig) {
        this.userRepository = userRepository;
        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
        this.pathConfig = pathConfig;
    }

    @Bean
    public UserDetailsService userDetailsService() {
        return username -> userRepository.findByEmail(username)
                .map(UserCredintials::new) // Wrap Student into StudentDetails
                .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + username));
    }


    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

//    @Bean
//    public AuthenticationEntryPoint authenticationEntryPoint() {
//        return (request, response, ex) -> {
//            response.setStatus(HttpStatus.UNAUTHORIZED.value());
//            response.setContentType("application/json");
//            response.setHeader("WWW-Authenticate", "");
//            response.getWriter().write("{\"error\": \"Unauthorized access\"}");
//        };
//    }

    /* todo
     2. make sure that the specific roles are the ones who runs the right apis
     3. make sure that calling apis return the right data
     */


    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http.authorizeHttpRequests(configurer ->
                configurer
                        // ! IMPORTANT: Order matters! Most specific rules first, then general rules

                        // 1. Admin-only paths (most specific first)
                        .requestMatchers(pathConfig.getAdminCourseFrontendPaths()).hasAnyAuthority("ROLE_ADMIN")
                        .requestMatchers(pathConfig.getAdminCourseApiPaths()).hasAnyAuthority("ROLE_ADMIN")
                        .requestMatchers(pathConfig.getAdminApiPath()).hasAnyAuthority("ROLE_ADMIN")

                        // 2. Role-specific paths
                        .requestMatchers(pathConfig.getStudentApiPath()).hasAnyAuthority("ROLE_STUDENT")
                        .requestMatchers(pathConfig.getTeacherApiPath()).hasAnyAuthority("ROLE_TEACHER")

                        // 3. Public paths (more general)
                        .requestMatchers(pathConfig.getPublicFrontEndPaths()).permitAll()
                        .requestMatchers(pathConfig.getPublicCourseFrontPaths()).permitAll()
                        .requestMatchers(pathConfig.getPublicCourseApiPaths()).permitAll()
                        .requestMatchers(pathConfig.getAllPublicPaths()).permitAll()

                        // 4. Authenticated paths (general)
                        .requestMatchers(pathConfig.getAuthFrontEndPaths()).authenticated()

                        // 5. All other requests require authentication
                        .anyRequest().authenticated());

        http.csrf(AbstractHttpConfigurer::disable);

        http.sessionManagement(session ->
                session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        http.exceptionHandling(exception -> exception
                .authenticationEntryPoint((request, response, authException) -> {
                    String requestedUri = request.getRequestURI();

                    // For API requests, return JSON error
                    if (requestedUri.startsWith("/api")) {
                        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                        response.setContentType("application/json");
                        response.getWriter().write("{\"error\":\"Unauthorized\",\"message\":\"Authentication required\"}");
                    } else {
                        // For page requests, redirect to login
                        response.sendRedirect(pathConfig.getHomePage());
                    }
                })
                .accessDeniedHandler((request, response, accessDeniedException) -> {
                    String requestedUri = request.getRequestURI();

                    // For API requests, return JSON error
                    if (requestedUri.startsWith("/api")) {
                        response.setStatus(HttpServletResponse.SC_FORBIDDEN);
                        response.setContentType("application/json");
                        response.getWriter().write("{\"error\":\"Forbidden\",\"message\":\"Access denied\"}");
                    } else {
                        // For page requests, show error page or redirect
                        response.sendRedirect(pathConfig.getHomePage());
                    }
                })
        );

        http.addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

}