package com.server.capple.domain.report.service;

import com.server.capple.domain.member.entity.Member;
import com.server.capple.domain.report.dto.reponse.ReportResponse;
import com.server.capple.domain.report.dto.request.ReportRequest;
import com.server.capple.domain.report.entity.Report;

public interface ReportService {

    Report findReport(Long reportId);
    ReportResponse.ReportId createReport(Member member, ReportRequest.ReportCreate request);

    ReportResponse.ReportInfos getReports(Member member);

    ReportResponse.ReportId updateReport(Member member, Long reportId, ReportRequest.ReportUpdate request);

    ReportResponse.ReportId resignReport(Member member, Long reportId);
}
