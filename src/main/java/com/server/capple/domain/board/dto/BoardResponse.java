package com.server.capple.domain.board.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

public class BoardResponse {

    @Getter
    @AllArgsConstructor
    public static class BoardId {
        private Long boardId;
    }
    @Getter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class BoardInfo {
        private Long boardId;
        private Long writerId;
        private String writerNickname;
        private String content;
        private Integer heartCount;
        private Integer commentCount;
        private LocalDateTime createdAt;
        private Boolean isMine;
        private Boolean isReported;
        private Boolean isLiked;
    }

    @Getter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class ToggleBoardHeart {
        private Long boardId;
        private Boolean isLiked;
    }
}
