package com.server.capple.domain.board.dto;

import com.server.capple.domain.board.entity.BoardType;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class BoardRequest {

    @Getter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class BoardCreate {
        @NotEmpty
        private String content;
        private BoardType boardType;
    }

    @Getter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class BoardUpdate {
        @NotNull
        private Long boardId;
        @NotEmpty
        private String content;
    }
}
