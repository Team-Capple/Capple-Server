package com.server.capple.domain.report.dto.reponse;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

public class ReportResponse {

    @Getter
    @AllArgsConstructor
    @Builder
    public static class ReportId {
        private Long reportId;
    }
}
