package com.server.capple.domain.boardReport.dto;

import com.server.capple.domain.board.entity.BoardType;
import com.server.capple.domain.boardReport.entity.BoardReportType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class BoardReportRequest {

    @Getter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class BoardReportCreate {
        Long memberId;
        Long boardId;
        BoardReportType boardReportType;
    }

    @Getter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class BoardReportUpdate {
        BoardReportType boardReportType;
    }
}
