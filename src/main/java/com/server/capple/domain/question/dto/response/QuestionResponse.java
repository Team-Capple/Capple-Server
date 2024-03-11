package com.server.capple.domain.question.dto.response;

import com.server.capple.domain.question.entity.QuestionStatus;

import java.time.LocalDateTime;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

public class QuestionResponse {


    @Getter
    @AllArgsConstructor
    @Builder
    public static class QuestionSummary {
        private Long questionId;
        private QuestionStatus questionStatus;
        private String content;
    }

    @Getter
    @AllArgsConstructor
    @Builder
    public static class QuestionInfo {
        private Long questionId;
        private QuestionStatus questionStatus;
        private LocalDateTime livedAt;
        private String content;
        private String tag;
        private Long likeCount;
        private Long commentCount;
    }

    @Getter
    @AllArgsConstructor
    @Builder
    public static class QuestionId {
        private Long questionId;
    }

    @Getter
    @AllArgsConstructor
    @Builder
    public static class QuestionInfos {
        private List<QuestionInfo> questionInfos;
    }
}
