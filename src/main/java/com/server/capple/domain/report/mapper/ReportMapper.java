package com.server.capple.domain.report.mapper;

import com.server.capple.domain.answer.entity.Answer;
import com.server.capple.domain.member.entity.Member;
import com.server.capple.domain.question.entity.Question;
import com.server.capple.domain.report.dto.request.ReportRequest;
import com.server.capple.domain.report.entity.Report;
import org.springframework.stereotype.Component;

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
}
