package com.server.capple.domain.question.dto.response;

import com.server.capple.domain.answer.entity.Answer;
import com.server.capple.domain.question.entity.QuestionStatus;
import java.util.List;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.experimental.SuperBuilder;

public class QuestionResponse {


    @Data
    @Builder
    public static class QuestionSummary {
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
