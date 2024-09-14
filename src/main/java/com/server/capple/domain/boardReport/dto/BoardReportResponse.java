package com.server.capple.domain.boardReport.dto;

import com.server.capple.domain.boardReport.entity.BoardReport;
import com.server.capple.domain.boardReport.entity.BoardReportType;
import com.server.capple.global.exception.errorCode.BoardReportErrorCode;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

public class BoardReportResponse {

    @Getter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class BoardReportCreate {
        Long boardReportId;
    }

    @Getter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class BoardReportsGet {
        List<BoardReportInfo> boardReportInfos;
    }

    @Getter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class BoardReportInfo {
        Long boardReportId;
        Long reporterId;
        Long boardId;
        BoardReportType boardReportType;
    }

    @Getter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class BoardReportUpdate {
        Long boardReportId;
    }

    @Getter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class BoardReportResign {
        Long boardReportId;
    }

}
