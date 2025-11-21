package com.app.coursecenter.response;


import com.app.coursecenter.entity.Authority;
import lombok.Data;

import java.util.List;

@Data
public class UserDetailResponse {

    private long id;

    private String firstName;

    private String lastName;

    private String email;

    private List<Authority> authorities;

}
