package com.server.capple.domain.answer.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

public class AnswerResponse {
    @Getter
    @AllArgsConstructor
    public static class AnswerId {
        private Long answerId;
    }
}
