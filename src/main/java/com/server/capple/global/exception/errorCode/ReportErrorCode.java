package com.server.capple.global.exception.errorCode;

import com.server.capple.global.exception.ErrorCode;
import com.server.capple.global.exception.ErrorCodeInterface;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ReportErrorCode implements ErrorCodeInterface {
    REPORT_NOT_FOUND("REPORT001", "신고가 존재하지 않습니다.", HttpStatus.NOT_FOUND),
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
