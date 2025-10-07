package com.app.coursecenter.repository;


import com.app.coursecenter.entity.Course;
import com.app.coursecenter.entity.Student;
import io.jsonwebtoken.lang.Assert;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Collections;
import java.util.Optional;
import java.util.Set;

import static org.hibernate.validator.internal.util.Contracts.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

@DataJpaTest

public class TestCourseStudentRelationShip {

    @Autowired
    CourseRepository courseRepository;

    @Autowired
    StudentRepository studentRepository;


    private Course produceOneCourse() {
        Course course = new Course();
        course.setName("CourseTest 1");
        course.setDescription("Description for Test 1");
        course.setCreditHours(5);
        return course;
    }

    private Student produceOneStudent() {
        Student student = new Student();
        student.setFirstName("John");
        student.setLastName("Doe");
        student.setEmail("john.doe@example.com");
        student.setPassword("password123");
        return student;
    }

    @Test
    public void testStudentCourseManyToManyRelationship() {
        // Create a student

        var student = produceOneStudent();

        // Create a course
        Course course = produceOneCourse();

        // Save course first to generate ID
        course = courseRepository.save(course);

        // Associate course with student
        student.setCourses(Collections.singleton(course));

        // Save student (which should also persist the relationship)
        student = studentRepository.save(student);

        // Clear the persistence context to ensure we're getting fresh data from the database
        studentRepository.flush();

        // Retrieve the student and verify the relationship
        Optional<Student> foundStudent = studentRepository.findById(student.getId());
        assertTrue(foundStudent.isPresent(), "Student should be found");

        Set<Course> studentCourses = foundStudent.get().getCourses();


        // Verify the inverse relationship
        Optional<Course> foundCourse = courseRepository.findById(course.getId());
        assertTrue(foundCourse.isPresent(), "Course should be found");

        Set<Student> courseStudents = foundCourse.get().getStudents();
        assertFalse(courseStudents.isEmpty(), "Course should have students");
        assertEquals(1, courseStudents.size(), "Course should have 1 student");
        assertEquals(student.getId(), courseStudents.iterator().next().getId(), "Student IDs should match");
    }


}
