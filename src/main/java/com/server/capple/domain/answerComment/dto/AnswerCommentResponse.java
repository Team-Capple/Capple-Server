package com.server.capple.domain.answerComment.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
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
    public static class AnswerCommentLike {
        private Long answerCommentId;
        private Boolean isLiked;
    }

    @Getter
    @AllArgsConstructor
    @Builder
    public static class AnswerCommentInfo {
        private Long answerCommentId;
        private Long writerId;
        private String content;
        private Integer heartCount;
        private Boolean isLiked;
        private Boolean isMine;
        private LocalDateTime createdAt;
    }

    @Getter
    @AllArgsConstructor
    public static class AnswerCommentInfos {
        private List<AnswerCommentInfo> answerCommentInfos;
    }
}
