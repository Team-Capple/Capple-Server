package com.server.capple.global.exception.errorCode;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class BaseException extends RuntimeException {
    private final String errorCode;
    private String message;
    private final HttpStatus status;

    public BaseException(ErrorCode code) {
        errorCode = code.getErrorcode();
        message = code.getMessage();
        status = code.getStatus();
    }
}
