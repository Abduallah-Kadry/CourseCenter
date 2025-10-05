package com.app.coursecenter.mapper;


import com.app.coursecenter.entity.Course;
import com.app.coursecenter.request.CreateCourseRequest;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")

public interface CourseMapper {

    Course courseRequestToCourse(CreateCourseRequest courseRequest);

}
