package com.app.coursecenter.mapper;

import com.app.coursecenter.dto.UserDto;
import com.app.coursecenter.entity.User;
import com.app.coursecenter.response.UserDetailResponse;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface UserMapper {

    User map(UserDto userDto);

    UserDto userToUserDTO(User user);

    UserDetailResponse userToUserDetailsResponse(User user);

    List<UserDto> map(List<User> users);
}
