package com.app.coursecenter.response;


import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;


@NoArgsConstructor
@Data
public class ApiRespond {
    private int status;
    private String message;
    private Object data;
    private long timestamp;

    public ApiRespond(HttpStatus status, String message, Object data) {
        this.status = status.value();
        this.message = message;
        this.data = data;
        this.timestamp = System.currentTimeMillis();
    }
}
