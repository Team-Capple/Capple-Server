package com.server.capple.global.common;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
@JsonPropertyOrder({"timeStamp", "code", "message", "result"})
public class BaseResponse<T> {

    private final LocalDateTime timeStamp = LocalDateTime.now();
    private final String code;
    private final String message;
    @JsonInclude(Include.NON_NULL)
    private T result;

    // 성공 시 응답
    public static <T> BaseResponse<T> onSuccess(T result) {
        return new BaseResponse<>("COMMON200", "요청에 성공하였습니다.", result);
    }

    public static <T> BaseResponse<T> onFailure(String code, String message, T data) {
        return new BaseResponse<>(code, message, data);
    }
}
