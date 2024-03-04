package com.server.capple.domain.question.dto.response;

import com.server.capple.domain.question.entity.QuestionStatus;
import lombok.Builder;
import lombok.Data;

public class QuestionResponse {


    @Data
    @Builder
    public static class MainQuestion {
        private Long questionId;
        private QuestionStatus questionStatus;
        private String content;
    }

    @Data
    @Builder
    public static class QuestionId {
        private Long questionId;
    }



}
