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
    public static class BoardCommentHeart {
        private Long boardCommentId;
        private Boolean isLiked;
    }

    @Getter
    @Builder
    public static class BoardCommentInfo {
        private Long boardCommentId;
        private String writer;
        private String content;
        private Long heartCount;
        private Boolean isLiked;
        private LocalDateTime createdAt;
    }

    @Getter
    @AllArgsConstructor
    public static class BoardCommentInfos {
        private List<BoardCommentInfo> boardCommentInfos;
    }
}
