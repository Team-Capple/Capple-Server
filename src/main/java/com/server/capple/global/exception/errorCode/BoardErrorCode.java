package com.server.capple.global.exception.errorCode;

import com.server.capple.global.exception.ErrorCode;
import com.server.capple.global.exception.ErrorCodeInterface;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum BoardErrorCode implements ErrorCodeInterface {
    BOARD_NOT_FOUND("BOARD001", "게시글이 존재하지 않습니다.", HttpStatus.NOT_FOUND),
    BOARD_BAD_REQUEST("BOARD002", "게시글에 내용이 없습니다.",HttpStatus.BAD_REQUEST),
    BOARD_NO_AUTHORIZATION("BOARD003", "해당 게시글에 삭제 권한이 없습니다.", HttpStatus.FORBIDDEN)
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
