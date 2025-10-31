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
import java.util.Set;

@Service
@RequiredArgsConstructor
public class CourseServiceImpl implements CourseService {

    private final CourseRepository courseRepository;
    private final CourseMapper courseMapper;

    @Override
    public Page<CourseDto> getAllCourses(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Course> coursePage = courseRepository.findAll(pageable);
        return coursePage.map(courseMapper::courseToCourseDto);
    }

    @Override
    public Course getCourseById(Long id) {
        return courseRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Course Not found!"));
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
    public Course updateCourse(Long id, CourseDto courseDto) {
        return courseRepository.save(courseMapper.courseDtoToCourse(courseDto));
    }

    @Override
    public Boolean deleteCourse(Long id) {
        courseRepository.deleteById(id);
    }

//    @Override
//    public Set<Course> getCoursesByStudentId(Long studentId) {
//        return courseRepository.findCoursesByStudentId(studentId);
//    }
}
