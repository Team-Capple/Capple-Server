package com.server.capple.domain.report.service;

import com.server.capple.domain.member.entity.Member;
import com.server.capple.domain.report.dto.reponse.ReportResponse;
import com.server.capple.domain.report.dto.request.ReportRequest;

public interface ReportService {
    ReportResponse.ReportId createReport(Member member, ReportRequest.ReportCreate request);

    ReportResponse.ReportInfos getReports(Member member);

}
