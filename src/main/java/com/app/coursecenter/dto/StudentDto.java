package com.app.coursecenter.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class StudentDto {

    private long id;

    private String firstName;

    private String lastName;

    private String Email;

    private int version;
}
