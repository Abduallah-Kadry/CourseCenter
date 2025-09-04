package com.app.coursecenter.service.courseservice;

import com.app.coursecenter.entity.Course;
import com.app.coursecenter.repository.CourseRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CourseServiceImpl implements CourseService {

    private final CourseRepository courseRepository;

    @Override
    public List<Course> getAllCourses() {
        return courseRepository.findAll();
    }

    @Override
    public Course getCourseById(Long id) {
        return null;
    }

    @Override
    public Course addCourse(Course course) {
        return null;
    }

    @Override
    public Course updateCourse(Long id, Course course) {
        return null;
    }

    @Override
    public Boolean deleteCourse(Long id) {
        return null;
    }
}
