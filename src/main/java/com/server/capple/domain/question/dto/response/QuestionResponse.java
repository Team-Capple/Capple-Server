package com.server.capple.domain.question.dto.response;

import com.server.capple.domain.question.entity.QuestionStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;

public class QuestionResponse {


    @Getter
    @AllArgsConstructor
    @Builder
    public static class MainQuestion {
        private Long questionId;
        private QuestionStatus questionStatus;
        private String content;
    }

    @Getter
    @AllArgsConstructor
    public static class QuestionId {
        private Long questionId;
    }



}
