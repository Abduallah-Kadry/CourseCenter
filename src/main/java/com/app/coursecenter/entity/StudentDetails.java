package com.app.coursecenter.entity;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;

public class StudentDetails implements UserDetails {

    private final Student student;

    public StudentDetails(Student student) {
        this.student = student;
    }

    public Student getStudent() {
        return student;
    }

    public Long getId() {
        return student.getId();
    }

    public String getEmail() {
        return student.getEmail();
    }

    public String getFullName() {
        return student.getFirstName() + " " + student.getLastName();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return student.getAuthorities();
    }

    @Override
    public String getPassword() {
        return student.getPassword();
    }

    @Override
    public String getUsername() {
        return student.getEmail();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
