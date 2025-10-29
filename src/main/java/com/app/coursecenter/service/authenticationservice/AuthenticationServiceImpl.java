package com.app.coursecenter.service.authenticationservice;

import com.app.coursecenter.entity.Authority;
import com.app.coursecenter.entity.Student;
import com.app.coursecenter.exception.InvalidCredentialException;
import com.app.coursecenter.repository.StudentRepository;
import com.app.coursecenter.request.AuthenticationRequest;
import com.app.coursecenter.request.RegisterRequest;
import com.app.coursecenter.response.AuthenticationResponse;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Service
public class AuthenticationServiceImpl implements AuthenticationService {

    private final StudentRepository studentRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;

    public AuthenticationServiceImpl(StudentRepository studentRepository, PasswordEncoder passwordEncoder, AuthenticationManager authenticationManager, JwtService jwtService) {
        this.studentRepository = studentRepository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
    }

    @Override
    @Transactional
    public void register(RegisterRequest input) {
        if (isEmailTaken(input.getEmail())) {
            throw new RuntimeException("Email already taken");
        }
        Student student = buildNewUser(input);
        studentRepository.save(student);
    }

    @Override
    @Transactional(readOnly = true)
    public AuthenticationResponse login(AuthenticationRequest request) {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));
            Student student = studentRepository.findByEmail(request.getEmail())
                    .orElseThrow(() -> new InvalidCredentialException("Invalid email or password"));

            String jwtToken = jwtService.generateToken(new HashMap<>(), student);
            return new AuthenticationResponse(jwtToken);

        } catch (Exception e) {
            throw new InvalidCredentialException("Invalid email or password");
        }

    }



    private boolean isEmailTaken(String email) {
        return studentRepository.findByEmail(email).isPresent();
    }

    private Student buildNewUser(RegisterRequest input) {
        Student student = new Student();
        student.setId(0);
        student.setFirstName(input.getFirstName());
        student.setLastName(input.getLastName());
        student.setEmail(input.getEmail());
        student.setPassword(passwordEncoder.encode(input.getPassword()));
        student.setAuthorities(initialAuthority());
        return student;
    }

    private List<Authority> initialAuthority() {
        boolean isFirstUser = studentRepository.count() == 0;
        List<Authority> authorities = new ArrayList<>();
        authorities.add(new Authority("ROLE_STUDENT"));
        if (isFirstUser) {
            authorities.add(new Authority("ROLE_ADMIN"));
        }
        return authorities;
    }
}