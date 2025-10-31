package com.app.coursecenter.service.courseservice;

import com.app.coursecenter.entity.Course;
import com.app.coursecenter.exception.ResourceNotFoundException;
import com.app.coursecenter.mapper.CourseMapper;
import com.app.coursecenter.repository.CourseRepository;
import com.app.coursecenter.request.CreateCourseRequest;
import com.app.coursecenter.request.UpdateCourseRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

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
        return courseRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Course Not found!"));
    }

    @Override
    public Course addCourse(CreateCourseRequest course) {
        return courseRepository.save(courseMapper.courseCreateRequestToCourse(course));
    }

    @Override
    public Course updateCourse(Long id, UpdateCourseRequest course) {
        return null;
    }

    @Override
    public Boolean deleteCourse(Long id) {
        return null;
    }

//    @Override
//    public Set<Course> getCoursesByStudentId(Long studentId) {
//        return courseRepository.findCoursesByStudentId(studentId);
//    }
}
