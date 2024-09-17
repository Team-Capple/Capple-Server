package com.server.capple.domain.boardComment.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

public class BoardCommentResponse {

    @Getter
    @AllArgsConstructor
    public static class BoardCommentId {
        private Long BoardCommentId;
    }

    @Getter
    @AllArgsConstructor
    public static class ToggleBoardCommentHeart {
        private Long boardCommentId;
        private Boolean isLiked;
    }

    @Getter
    @Builder
    public static class BoardCommentInfo {
        private Long boardCommentId;
        private Long writerId;
        private String content;
        private Integer heartCount;
        private Boolean isLiked;
        private Boolean isMine;
        private Boolean isReport;
        private LocalDateTime createdAt;
    }
}
