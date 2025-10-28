package com.app.coursecenter.service.courseservice;

import com.app.coursecenter.dto.CourseDto;
import com.app.coursecenter.entity.Course;
import com.app.coursecenter.request.CreateCourseRequest;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Set;

public interface CourseService {
    Page<CourseDto> getAllCourses(int page, int size);

    CourseDto getCourseById(Long id);


    Course addCourse(CreateCourseRequest course);

    Course updateCourse(Long id, CourseDto courseDto);

    void deleteCourse(Long id);

    //Set<Course> getCoursesByStudentId(Long studentId);
}
