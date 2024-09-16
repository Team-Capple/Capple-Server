package com.server.capple.domain.board.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

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
    public static class BoardsGetBoardInfos {
        private List<BoardsGetBoardInfo> boards;
    }

    @Getter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class BoardsGetBoardInfo {
        private Long boardId;
        private Long writerId;
        private String content;
        private Integer heartCount;
        private Integer commentCount;
        private LocalDateTime createAt;
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
