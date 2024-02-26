package com.server.capple.domain.question.dto;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;

public class QuestionResponse {

    @Data
    public static class  QuestionId {
        private Long questionId;
    }
}
