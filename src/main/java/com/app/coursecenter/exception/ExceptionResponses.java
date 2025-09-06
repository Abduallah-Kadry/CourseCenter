package com.app.coursecenter.exception;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ExceptionResponses {

    private int status;
    private String message;
    private long timestamp;
}
