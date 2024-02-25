package com.server.capple.global.exception.errorCode;

import org.springframework.http.HttpStatus;

public interface ErrorCode {
    String getErrorcode();

    String getMessage();

    HttpStatus getStatus();
}
