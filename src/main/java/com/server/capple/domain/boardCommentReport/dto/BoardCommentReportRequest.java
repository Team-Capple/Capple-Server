package com.server.capple.domain.boardCommentReport.dto;

import com.server.capple.domain.boardCommentReport.entity.BoardCommentReportType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class BoardCommentReportRequest {
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class BoardCommentReportCreate {
        Long boardCommentId;
        BoardCommentReportType boardCommentReportType;
    }
}
