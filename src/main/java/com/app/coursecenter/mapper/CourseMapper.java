package com.app.coursecenter.mapper;


import com.app.coursecenter.dto.CourseDto;
import com.app.coursecenter.entity.Course;
import com.app.coursecenter.request.CreateCourseRequest;
import com.app.coursecenter.request.UpdateCourseRequest;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")

public interface CourseMapper {

    Course courseCreateRequestToCourse(CreateCourseRequest courseRequest);
    Course courseUpdateRequestToCourse(UpdateCourseRequest courseRequest);

    CourseDto courseToCourseDto(Course course);

    Course courseDtoToCourse(CourseDto courseDto);
}