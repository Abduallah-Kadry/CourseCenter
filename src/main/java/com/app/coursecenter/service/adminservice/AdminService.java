package com.app.coursecenter.service.adminservice;

import com.app.coursecenter.dto.UserDto;
import org.springframework.data.domain.Page;



public interface AdminService {

    Page<UserDto> getAllUser(int page, int size);

    UserDto promoteToAdmin(long studentId);

    void deleteNonAdminUser(long id);
}
