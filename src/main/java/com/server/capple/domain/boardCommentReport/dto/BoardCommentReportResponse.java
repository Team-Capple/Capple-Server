package com.server.capple.domain.boardCommentReport.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

public class BoardCommentReportResponse {

    @Getter
    @AllArgsConstructor
    public static class BoardCommentReportCreate {
        Long boardCommentReportId;
    }
}
