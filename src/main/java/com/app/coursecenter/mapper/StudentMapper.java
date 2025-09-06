package com.app.coursecenter.mapper;

import com.app.coursecenter.dto.StudentDto;
import com.app.coursecenter.entity.Student;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface StudentMapper {

    Student map(StudentDto studentDto);

    //TODO the fokin mapper doesn't get me the first name and last name although they are typical written  in Student and StudentDto
    StudentDto map(Student student);

    List<StudentDto> map(List<Student> students);
}
