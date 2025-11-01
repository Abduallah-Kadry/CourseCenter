package com.app.coursecenter.mapper;


import com.app.coursecenter.dto.CourseDto;
import com.app.coursecenter.entity.Course;
import com.app.coursecenter.request.CreateCourseRequest;
import com.app.coursecenter.request.UpdateCourseRequest;
import com.app.coursecenter.response.CourseResponse;
import com.app.coursecenter.response.UserCoursesRespond;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")

public interface CourseMapper {

    Course courseCreateRequestToCourse(CreateCourseRequest courseRequest);
    Course courseUpdateRequestToCourse(UpdateCourseRequest courseRequest);

    CourseResponse courseToCourseResponse(Course course);

    Course courseDtoToCourse(CourseDto courseDto);

    UserCoursesRespond courseToUserCoursesRespond(Course course);

}