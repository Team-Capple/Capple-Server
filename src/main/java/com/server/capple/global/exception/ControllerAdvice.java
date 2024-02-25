package com.server.capple.global.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ControllerAdvice {

    /**
     * BaseException
     */
    @ExceptionHandler(BaseException.class)
    public ResponseEntity<ExceptionResponse> baseExceptionHandler(BaseException e) {
        return new ResponseEntity<>(new ExceptionResponse(e.getErrorCode(), e.getMessage()), e.getStatus());
    }
}
