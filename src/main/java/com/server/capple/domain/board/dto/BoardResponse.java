package com.server.capple.domain.board.dto;

import com.server.capple.domain.member.entity.Member;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
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
        private List<BoardsGetByBoardTypeBoardInfo> boards = new ArrayList<>();
    }

    @Getter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class BoardsGetByBoardTypeBoardInfo {
        private Long writerId;
        private String content;
        private Integer heartCount;
        private Integer commentCount;
        private LocalDateTime createAt;
    }
}
