package com.server.capple.domain.board.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

public class BoardResponse {

    @Getter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class BoardCreate {
        private Long boardId;
    }

    @Getter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class BoardsGetByBoardType {
        private List<BoardsGetByBoardTypeBoardInfo> boards;
    }

    @Getter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class BoardsGetByBoardTypeBoardInfo {
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
    public static class BoardDelete {
        private Long boardId;
    }

    @Getter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class BoardsSearchByKeyword {
        private List<BoardsSearchByKeywordBoardInfo> boards;
    }

    @Getter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class BoardsSearchByKeywordBoardInfo {
        private Long boardId;
        private Long writerId;
        private String content;
        private Integer heartCount;
        private Integer commentCount;
        private LocalDateTime createAt;
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
