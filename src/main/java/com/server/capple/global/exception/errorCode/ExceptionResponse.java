package com.server.capple.global.exception.errorCode;

import java.time.LocalDateTime;
import lombok.Getter;

@Getter
public class ExceptionResponse {
    private final String code;
    private final String message;
    private final LocalDateTime timeStamp;

    public ExceptionResponse(String code, String message) {
        this.code = code;
        this.message = message;
        this.timeStamp = LocalDateTime.now();
    }
}
