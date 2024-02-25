package com.server.capple.global.exception;

import com.server.capple.global.common.BaseResponse;
import com.server.capple.global.exception.errorCode.GlobalErrorCode;
import java.util.Map;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.NoHandlerFoundException;

@RestController
public class ControllerAdvice {

    /**
     * 정의한 RestApiException 예외 처리
     */
    @ExceptionHandler(value = RestApiException.class)
    public ResponseEntity<BaseResponse<String>> handleRestApiException(
            RestApiException e) {
        ErrorCode errorCode = e.getErrorCode();
        return handlerExceptionInternal(errorCode);
    }

    /**
     * 일반적인 서버 에러 처리
     */
    @ExceptionHandler
    public ResponseEntity<BaseResponse<String>> handleException(
            Exception e) {
        e.printStackTrace();
        return handleExceptionInternalFalse(GlobalErrorCode.SERVER_ERROR.getErrorCode(), e.getMessage());
    }

    /**
     * MethodArgumentTypeMismatchException 발생 시 예외 처리
     * 메서드의 인자 타입이 예상과 다른 경우
     */
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<BaseResponse<String>> handleMethodArgumentTypeMismatch(MethodArgumentTypeMismatchException e) {
        // 예외 처리 로직
        return handleExceptionInternal(GlobalErrorCode.NOT_VALID_ARGUMENT_ERROR.getErrorCode());
    }

    

    private ResponseEntity<BaseResponse<String>> handlerExceptionInternal(
            ErrorCode errorCode) {
        return ResponseEntity
                .status(errorCode.getHttpStatus().value())
                .body(BaseResponse.onFailure(errorCode.getCode(), errorCode.getMessage(), null));
    }

    private ResponseEntity<BaseResponse<String>> handleExceptionInternal(ErrorCode errorCode) {
        return ResponseEntity
                .status(errorCode.getHttpStatus().value())
                .body(BaseResponse.onFailure(errorCode.getCode(), errorCode.getMessage(), null));
    }

    private ResponseEntity<Object> handlerExceptionInternalArgs(
            ErrorCode errorCode,
            Map<String, String> errorArgs) {
        return ResponseEntity
                .status(errorCode.getHttpStatus().value())
                .body(BaseResponse.onFailure(errorCode.getCode(), errorCode.getMessage(), errorArgs));
    }
    private ResponseEntity<BaseResponse<String>> handleExceptionInternalFalse(
            ErrorCode errorCode,
            String errorPoint) {
        return ResponseEntity
                .status(errorCode.getHttpStatus().value())
                .body(BaseResponse.onFailure(errorCode.getCode(), errorCode.getMessage(), errorPoint));
    }
}
