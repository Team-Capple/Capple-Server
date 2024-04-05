package com.server.capple.global.exception.errorCode;

import com.server.capple.global.exception.ErrorCode;
import com.server.capple.global.exception.ErrorCodeInterface;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum AppleOauthError implements ErrorCodeInterface {
    BAD_REQUEST("OAUTH001", "잘못된 데이터 이거나 잘못된 형식의 접근입니다.",HttpStatus.BAD_REQUEST)
    ,FORBIDDEN("OAUTH003", "애플 인증서버 접근 권한이 없습니다.",HttpStatus.FORBIDDEN)
    ,NOT_FOUND("OAUTH004", "애플 인증서버 요청 클라이언트 오류입니다.",HttpStatus.NOT_FOUND)
    ,INTERNAL_SERVER_ERROR("OAUTH005", "애플 인증서버 내부 오류입니다.",HttpStatus.INTERNAL_SERVER_ERROR)
    ,
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
