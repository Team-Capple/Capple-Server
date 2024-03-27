package com.server.capple.global.exception.errorCode;

import com.server.capple.global.exception.ErrorCode;
import com.server.capple.global.exception.ErrorCodeInterface;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum MailErrorCode implements ErrorCodeInterface {
    MULTI_PART_CRAETION_FAILED("MAIL001", "Multipart 생성에 실패했습니다.", HttpStatus.INTERNAL_SERVER_ERROR),
    INVALID_EMAIL_FORM("MAIL002", "이메일 형식이 올바르지 않습니다.", HttpStatus.BAD_REQUEST),
    NOT_SUPPORTED_EMAIL_DOMAIN("MAIL003", "지원하지 않는 이메일 도메인입니다.", HttpStatus.BAD_REQUEST),
    DUPLICATE_EMAIL("MAIL004", "이미 가입된 이메일입니다.", HttpStatus.BAD_REQUEST),
    ;
    private final String code;
    private final String message;
    private final HttpStatus httpStatus;

    @Override
    public ErrorCode getErrorCode() {
        return ErrorCode.builder()
            .code(code)
            .message(message)
            .httpStatus(httpStatus)
            .build();
    }
}
