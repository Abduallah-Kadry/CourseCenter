package com.app.coursecenter.service.courseservice;

import com.app.coursecenter.entity.Course;
import com.app.coursecenter.request.CreateCourseRequest;

import java.util.List;
import java.util.Set;

public interface CourseService {
    List<Course> getAllCourses();

    Course getCourseById(Long id);


    Course addCourse(CreateCourseRequest course);

    Course updateCourse(Long id, Course course);

    Boolean deleteCourse(Long id);


    Set<Course> getCoursesByStudentId(Long studentId);
}
