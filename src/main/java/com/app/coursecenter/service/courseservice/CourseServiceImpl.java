package com.app.coursecenter.service.courseservice;

import com.app.coursecenter.entity.Course;
import com.app.coursecenter.mapper.CourseMapper;
import com.app.coursecenter.repository.CourseRepository;
import com.app.coursecenter.request.CreateCourseRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class CourseServiceImpl implements CourseService {

    private final CourseRepository courseRepository;
    private final CourseMapper courseMapper;

    @Override
    public List<Course> getAllCourses() {
        return courseRepository.findAll();
    }

    @Override
    public Course getCourseById(Long id) {
        return null;
    }

    @Override
    public Course addCourse(CreateCourseRequest course) {
        try {
            return courseRepository.save(courseMapper.courseRequestToCourse(course));
        } catch (Exception e) {
            throw e;
        }
    }

    @Override
    public Course updateCourse(Long id, Course course) {
        return null;
    }

    @Override
    public Boolean deleteCourse(Long id) {
        return null;
    }

    @Override
    public Set<Course> getCoursesByStudentId(Long studentId) {
        return courseRepository.findCoursesByStudentId(studentId);
    }
}
