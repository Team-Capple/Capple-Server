package com.server.capple.global.exception.errorCode;

import com.server.capple.global.exception.ErrorCode;
import com.server.capple.global.exception.ErrorCodeInterface;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum CommentErrorCode implements ErrorCodeInterface {
    COMMENT_NOT_FOUND("COMMENT001", "댓글을 찾을 수 없습니다.", HttpStatus.NOT_FOUND),
    COMMENT_UNAUTHORIZED("COMMENT002", "댓글에 대한 권한이 없습니다.", HttpStatus.FORBIDDEN),
    COMMENT_COUNT_INCREASE_FAILED("COMMENT003", "댓글 생성에 실패했습니다.", HttpStatus.LOCKED),
    COMMENT_COUNT_DECREASE_FAILED("COMMENT004", "댓글 제거에 실패했습니다.", HttpStatus.LOCKED),
    COMMENT_HEART_CHANGE_FAILED("COMMENT005", "댓글 좋아요 수정에 실패했습니다.", HttpStatus.LOCKED),
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
