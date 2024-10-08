package com.server.capple.domain.question.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.server.capple.domain.question.entity.QuestionStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

public class QuestionResponse {

    @Getter
    @AllArgsConstructor
    @Builder
    public static class QuestionSummary {
        private Long questionId;
        private QuestionStatus questionStatus;
        private String content;
//        private Integer likeCount;
//        private Integer commentCount;
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
//        private Integer likeCount;
//        private Integer commentCount;
        private Boolean isAnswered;
    }

    @Getter
    @AllArgsConstructor
    @Builder
    public static class QuestionId {
        private Long questionId;
    }

    @Getter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class QuestionToggleHeart {
        private Long questionId;
        private Boolean isLiked;
    }
}
