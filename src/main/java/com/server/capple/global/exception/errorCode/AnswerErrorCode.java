package com.server.capple.global.exception.errorCode;

import com.server.capple.global.exception.ErrorCode;
import com.server.capple.global.exception.ErrorCodeInterface;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum AnswerErrorCode implements ErrorCodeInterface {
    ANSWER_NOT_FOUND("ANSWER001", "답변을 찾을 수 없습니다.", HttpStatus.NOT_FOUND),
    ANSWER_UNAUTHORIZED("ANSWER002", "답변에 대한 권한이 업습니다.", HttpStatus.FORBIDDEN),
    ANSWER_ALREADY_EXIST("ANSWER003", "이미 답변한 질문입니다.", HttpStatus.BAD_REQUEST),
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
