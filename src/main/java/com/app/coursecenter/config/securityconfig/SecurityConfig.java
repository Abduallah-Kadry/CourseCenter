package com.app.coursecenter.config.securityconfig;


import com.app.coursecenter.repository.StudentRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
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
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final StudentRepository studentRepository;
    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    public SecurityConfig(StudentRepository studentRepository, JwtAuthenticationFilter jwtAuthenticationFilter) {
        this.studentRepository = studentRepository;
        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
    }

    @Bean
    public UserDetailsService userDetailsService() {
        return username -> studentRepository.findByEmail(username)
                .map(StudentDetails::new) // Wrap Student into StudentDetails
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

    @Bean
    public AuthenticationEntryPoint authenticationEntryPoint() {
        return (request, response, ex) -> {
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            response.setContentType("application/json");
            response.setHeader("WWW-Authenticate", "");
            response.getWriter().write("{\"error\": \"Unauthorized access\"}");
        };
    }

    /* todo
     1. make sure that the login return a token that the browser uses until it expire or being destroyed by the logout
     2. make sure that the specific roles are the ones who runs the right apis
     3. make sure that calling apis return the right data

     */


    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests(configurer ->
                configurer
                        .requestMatchers(pathConfig.getAllPublicPaths()).permitAll()
                        .requestMatchers(pathConfig.getPublicFrontEndPaths()).permitAll()
                        .requestMatchers(pathConfig.getAuthFrontEndPaths()).authenticated()
                        .requestMatchers(pathConfig.getAdminApiPath()).hasRole("ADMIN")
                        .requestMatchers(pathConfig.getStudentApiPath()).hasRole("STUDENT")
                        .requestMatchers(pathConfig.getTeacherApiPath()).hasRole("Teacher")
                        .anyRequest().authenticated());

        http.csrf(AbstractHttpConfigurer::disable);

        http.exceptionHandling(exceptionHandling ->
                exceptionHandling
                        .authenticationEntryPoint(authenticationEntryPoint()));

        http.sessionManagement(session ->
                session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        http.addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

}