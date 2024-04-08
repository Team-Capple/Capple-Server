package com.server.capple.domain.report.mapper;

import com.server.capple.domain.answer.entity.Answer;
import com.server.capple.domain.member.entity.Member;
import com.server.capple.domain.question.entity.Question;
import com.server.capple.domain.report.dto.reponse.ReportResponse;
import com.server.capple.domain.report.dto.request.ReportRequest;
import com.server.capple.domain.report.entity.Report;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ReportMapper {

    public Report toReport(Member member, Question question, Answer answer, ReportRequest.ReportCreate request) {
        return Report.builder()
                .member(member)
                .question(question)
                .answer(answer)
                .reportType(request.getReportType())
                .build();
    }

    public ReportResponse.ReportInfo toReportInfo(Member member, Report report) {
        return ReportResponse.ReportInfo.builder()
                .reportId(report.getId())
                .writer(report.getMember().getId())
                .questionId(report.getQuestion() == null ? null : report.getQuestion().getId())
                .answerId(report.getAnswer() == null ? null : report.getAnswer().getId())
                .reportType(report.getReportType())
                .isMine(member.getId() == report.getId())
                .build();
    }

    public ReportResponse.ReportInfos toReportInfos(List<ReportResponse.ReportInfo> reportInfos) {
        return ReportResponse.ReportInfos.builder()
                .reportInfos(reportInfos)
                .build();
    }
}
