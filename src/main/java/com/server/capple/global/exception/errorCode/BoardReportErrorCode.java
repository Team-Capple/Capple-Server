package com.server.capple.global.exception.errorCode;

import com.server.capple.global.exception.ErrorCode;
import com.server.capple.global.exception.ErrorCodeInterface;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum BoardReportErrorCode implements ErrorCodeInterface {
    BOARD_REPORT_NOT_FOUND("BOARDREPORT001", "게시글 신고가 존재하지 않습니다.", HttpStatus.NOT_FOUND),
    BOARD_REPORT_ALREADY_EXIST("BOARDREPORT002", "이미 신고한 게시글입니다.", HttpStatus.ALREADY_REPORTED),
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