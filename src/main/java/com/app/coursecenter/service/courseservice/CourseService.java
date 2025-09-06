package com.app.coursecenter.service.courseservice;

import com.app.coursecenter.entity.Course;

import java.util.List;

public interface CourseService {
    List<Course> getAllCourses();

    Course getCourseById(Long id);

    Course addCourse(Course course);

    Course updateCourse(Long id, Course course);

    Boolean deleteCourse(Long id);


}
