package com.server.capple.global.exception.errorCode;

import com.server.capple.global.exception.ErrorCode;
import com.server.capple.global.exception.ErrorCodeInterface;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum BoardCommentReportErrorCode implements ErrorCodeInterface {
    COMMENT_REPORT_ALREADY_EXIST("COMMENT_REPORT_001", "이미 신고한 댓글입니다.", HttpStatus.ALREADY_REPORTED),
    COMMENT_REPORT_NOT_FOUND("COMMENT_REPORT_002", "댓글에 대한 신고를 찾을 수 없습니다.", HttpStatus.NOT_FOUND)
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
