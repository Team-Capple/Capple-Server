package com.server.capple.domain.question.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
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
        private Boolean isAnswered;
    }

    @Getter
    @AllArgsConstructor
    @Builder
    public static class QuestionInfo {
        private Long questionId;
        private QuestionStatus questionStatus;

        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
        private LocalDateTime livedAt;
        private String content;
        private String tag;
        // 추후 추가 예정
//        private Long likeCount;
//        private Long commentCount;
        private Boolean isAnswered;
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
