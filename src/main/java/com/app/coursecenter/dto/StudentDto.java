package com.app.coursecenter.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class StudentDto {

    private long id;

    private String first_name;

    private String last_name;

    private String email;

    private int version;
}
