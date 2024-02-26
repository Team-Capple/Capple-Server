package com.server.capple.global.exception;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class RestApiException extends RuntimeException {
    private final ErrorCodeInterface errorCode;

    public ErrorCode getErrorCode() {
        return this.errorCode.getErrorCode();
    }
}
