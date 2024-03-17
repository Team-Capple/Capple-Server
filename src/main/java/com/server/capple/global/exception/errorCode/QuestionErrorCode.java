package com.server.capple.global.exception.errorCode;

import com.server.capple.global.exception.ErrorCode;
import com.server.capple.global.exception.ErrorCodeInterface;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum QuestionErrorCode implements ErrorCodeInterface {
    QUESTION_NOT_FOUND("QUESTION001", "질문이 존재하지 않습니다.", HttpStatus.NOT_FOUND),
    QUESTION_LIVE_NOT_FOUND("QUESTION002", "QUESTION 상태가 LIVE인 질문이 없습니다",HttpStatus.NOT_FOUND),
    QUESTION_PENDING_NOT_FOUND("QUESTION003", "QUESTION 상태가 PENDING인 질문이 없습니다.", HttpStatus.NOT_FOUND)
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
