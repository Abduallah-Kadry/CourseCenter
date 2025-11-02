package com.app.coursecenter.repository;


import com.app.coursecenter.entity.Course;
import com.app.coursecenter.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.hibernate.validator.internal.util.Contracts.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

@DataJpaTest

// generated ai test for the course and student relationship that i didn't decide


public class TestCourseStudentRelationShip {

    @Autowired
    CourseRepository courseRepository;

    @Autowired
    UserRepository userRepository;


    private Course produceOneCourse() {
        Course course = new Course();
        course.setName("CourseTest 1");
        course.setDescription("Description for Test 1");
        course.setCreditHours(5);
        return course;
    }

    private User produceOneStudent() {
        User user = new User();
        user.setFirstName("John");
        user.setLastName("Doe");
        user.setEmail("john.doe@example.com");
        user.setPassword("password123");
        return user;
    }

}
