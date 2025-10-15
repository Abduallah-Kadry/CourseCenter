package com.app.coursecenter.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;


// 1. case to use the dto is to use it to send data to the front

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode // very advisable to be used in DTOs
public class StudentDto {

    private long id;

    private String firstName;

    private String lastName;

    private String email;

    private int version;

}
