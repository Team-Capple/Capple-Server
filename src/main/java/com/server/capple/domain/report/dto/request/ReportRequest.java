package com.server.capple.domain.report.dto.request;

import com.server.capple.domain.report.entity.ReportType;
import lombok.Data;

public class ReportRequest {

    @Data
    public static class ReportCreate {
        private Long questionId;
        private Long answerId;
        private ReportType reportType;

//        TODO 추후 추가 예정
//        private String title;
//        private String content;
    }
}
