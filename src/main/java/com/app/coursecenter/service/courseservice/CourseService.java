package com.app.coursecenter.service.courseservice;

import com.app.coursecenter.request.CreateCourseRequest;
import com.app.coursecenter.request.UpdateCourseRequest;
import com.app.coursecenter.response.CourseResponse;
import org.springframework.data.domain.Page;

public interface CourseService {
    Page<CourseResponse> getAllCourses(int page, int size);

    CourseResponse getCourseById(Long id);

    CourseResponse addCourse(CreateCourseRequest course);

    CourseResponse updateCourse(Long id, UpdateCourseRequest courseDto);

    Boolean deleteCourse(Long id);

    //Set<Course> getCoursesByStudentId(Long studentId);
}
