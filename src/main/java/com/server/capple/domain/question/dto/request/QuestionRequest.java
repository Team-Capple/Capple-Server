package com.server.capple.domain.question.dto.request;

import com.server.capple.domain.question.entity.QuestionStatus;
import lombok.Data;

public class QuestionRequest {

    @Data
    public static class QuestionCreate {
        private QuestionStatus questionStatus;
        private String content;
    }
}
