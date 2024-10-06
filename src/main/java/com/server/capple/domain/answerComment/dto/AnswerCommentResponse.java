package com.server.capple.domain.answerComment.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

public class AnswerCommentResponse {
    @Getter
    @AllArgsConstructor
    public static class AnswerCommentId {
        private Long answerCommentId;
    }

    @Getter
    @AllArgsConstructor
    public static class AnswerCommentHeart {
        private Long answerCommentId;
        private Boolean isLiked;
    }

    @Getter
    @Builder
    public static class AnswerCommentInfo {
        private Long answerCommentId;
        private Long writerId;
        private String content;
        private Long heartCount;
        private LocalDateTime createdAt;

    }

    @Getter
    @AllArgsConstructor
    public static class AnswerCommentInfos {
        private List<AnswerCommentInfo> answerCommentInfos;
    }
}
