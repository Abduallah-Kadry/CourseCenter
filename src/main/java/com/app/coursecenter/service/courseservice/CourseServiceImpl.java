package com.app.coursecenter.service.courseservice;

import com.app.coursecenter.entity.Course;
import com.app.coursecenter.exception.DuplicateResourceException;
import com.app.coursecenter.exception.ResourceNotFoundException;
import com.app.coursecenter.mapper.CourseMapper;
import com.app.coursecenter.repository.CourseRepository;
import com.app.coursecenter.request.CreateCourseRequest;
import com.app.coursecenter.request.UpdateCourseRequest;
import com.app.coursecenter.response.CourseResponse;
import com.app.coursecenter.service.FileStorageService.FileStorageService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class CourseServiceImpl implements CourseService {

    private final CourseRepository courseRepository;
    private final CourseMapper courseMapper;

    // todo let a file storage system add the course image instead of using static resources
    private final FileStorageService fileStorageService;

    @Override
    public Page<CourseResponse> getAllCourses(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Course> coursePage = courseRepository.findAll(pageable);
        return coursePage.map(courseMapper::courseToCourseResponse);
    }

    @Override
    public CourseResponse getCourseById(Long id) {
        return courseMapper.courseToCourseResponse(
                courseRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Course Not found!")));
    }

    @Override
    public CourseResponse addCourse(CreateCourseRequest courseRequest) {

        if (courseRepository.findCourseByName(courseRequest.getName()).isPresent()) {
            throw new DuplicateResourceException("Course Already Exists by this name!");
        }


        Course course = courseRepository.save(courseMapper.courseCreateRequestToCourse(courseRequest));
        course.setRating(0); // the default of the Integer class is null
        return courseMapper.courseToCourseResponse(course);

    }

    @Override
    public CourseResponse updateCourse(Long id, UpdateCourseRequest courseUpdateRequest) {

        // courseRequest maybe different from courseResponse in the future
        Course course = courseRepository.save(courseMapper.courseUpdateRequestToCourse(courseUpdateRequest));
        return courseMapper.courseToCourseResponse(course);

    }

    @Override
    public Boolean deleteCourse(Long id) {
        if (getCourseById(id) != null) {
            courseRepository.deleteById(id);
            return true;
        }
        return false;
    }

}
