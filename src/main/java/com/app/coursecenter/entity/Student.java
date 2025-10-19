package com.app.coursecenter.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;
import java.util.Set;

// TODO see if using another wrapper class named MyStudentDetails that implements UserDetails to decouple it from security configs

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "student")
@Entity
public class Student extends BaseEntity implements UserDetails {
// java best practice, mapstruct (map by fixed string), hibernate (create delete)

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(nullable = false)
    private long id;

    @Column(nullable = false)
    private String firstName; // firstName

    @Column(nullable = false)
    private String lastName;

    @Column(unique = true, length = 100, nullable = false)
    private String email;

    @Column(nullable = false)
    private String password;

    // no cascading operations is being done you sync the data between the 2 tables yourself

//    @ManyToMany
//    @JoinTable(
//            name = "student_courses", // make new table
//            joinColumns = @JoinColumn(name = "student_id"), // make new column in to fk to the current table
//            inverseJoinColumns = @JoinColumn(name = "course_id") // make new column to fk to the other table
//    )
//    private Set<Course> courses;

    // student is the owning side so put the add, remove methods here

//    public void addCourse(Course course) {
//        courses.add(course);
//        course.getStudents().add(this);
//    }
//
//    public void removeCourse(Course course) {
//        courses.remove(course);
//        course.getStudents().remove(this);
//    }

    /* optimistic look .... version for 2 people who commit in the same time
        increase in value for every change
        [[SQL DB Lock]]
     */
    @Version
    private int version;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "user_authorities", joinColumns = @JoinColumn(name = "user_id"))
    private List<Authority> authorities;

    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return UserDetails.super.isAccountNonExpired();
    }

    @Override
    public boolean isAccountNonLocked() {
        return UserDetails.super.isAccountNonLocked();
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return UserDetails.super.isCredentialsNonExpired();
    }

    @Override
    public boolean isEnabled() {
        return UserDetails.super.isEnabled();
    }
}
