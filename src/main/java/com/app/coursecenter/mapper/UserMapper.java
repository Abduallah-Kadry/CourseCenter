package com.app.coursecenter.mapper;

import com.app.coursecenter.dto.UserDto;
import com.app.coursecenter.entity.User;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface UserMapper {

    User map(UserDto userDto);

    UserDto map(User user);

    List<UserDto> map(List<User> users);
}
