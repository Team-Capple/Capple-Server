package com.server.capple.domain.report.dto.reponse;

import com.server.capple.domain.report.entity.ReportType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

public class ReportResponse {

    @Getter
    @AllArgsConstructor
    @Builder
    public static class ReportId {
        private Long reportId;
    }

    @Getter
    @AllArgsConstructor
    @Builder
    public static class ReportInfo {
        private Long reportId;
        private Long writer;
        private Long questionId;
        private Long answerId;
        private ReportType reportType;
        private Boolean isMine;
//        TODO: 추후 개발 예정
//        private String title;
//        private String content;
    }

    @Getter
    @AllArgsConstructor
    @Builder
    public static class ReportInfos {
        private List<ReportInfo> reportInfos;
    }
}
