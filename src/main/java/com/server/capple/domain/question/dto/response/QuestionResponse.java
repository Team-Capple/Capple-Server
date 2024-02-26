package com.server.capple.domain.question.dto.response;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;

public class QuestionResponse {

    @Data
    @Builder
    public static class  QuestionId {
        private Long questionId;
    }
}
