package com.server.capple.domain.report.service;

import com.server.capple.domain.answer.entity.Answer;
import com.server.capple.domain.answer.service.AnswerService;
import com.server.capple.domain.member.entity.Member;
import com.server.capple.domain.member.service.MemberService;
import com.server.capple.domain.question.entity.Question;
import com.server.capple.domain.question.service.QuestionService;
import com.server.capple.domain.report.dto.reponse.ReportResponse;
import com.server.capple.domain.report.dto.request.ReportRequest;
import com.server.capple.domain.report.entity.Report;
import com.server.capple.domain.report.mapper.ReportMapper;
import com.server.capple.domain.report.repository.ReportRepository;
import com.server.capple.global.exception.RestApiException;
import com.server.capple.global.exception.errorCode.ReportErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ReportServiceImpl implements ReportService {

    private final ReportRepository reportRepository;
    private final ReportMapper reportMapper;
    private final MemberService memberService;
    private final QuestionService questionService;
    private final AnswerService answerService;

    @Override
    @Transactional
    public ReportResponse.ReportId createReport(Member loginMember, ReportRequest.ReportCreate request) {

        Member member = memberService.findMember(loginMember.getId());
        Question question = null;
        Answer answer = null;
        if (request.getQuestionId() != null) {
            question = questionService.findQuestion(request.getQuestionId());
        } else if (request.getAnswerId() != null) {
            answer = answerService.findAnswer(request.getAnswerId());
        }

        Report report = reportRepository.save(reportMapper.toReport(member, question, answer, request));

        return new ReportResponse.ReportId(report.getId());
    }

    @Override
    public ReportResponse.ReportInfos getReports(Member loginMember) {
        Member member = memberService.findMember(loginMember.getId());

        List<Report> reports = reportRepository.findAll();
        return reportMapper.toReportInfos(reports
                .stream()
                .map(report -> reportMapper.toReportInfo(member, report))
                .toList());
    }

    @Override
    @Transactional
    public ReportResponse.ReportId updateReport(Member loginMember, Long reportId, ReportRequest.ReportUpdate request) {
        Member member = memberService.findMember(loginMember.getId());

        Report report = reportRepository.findById(reportId).orElseThrow(()
        -> new RestApiException(ReportErrorCode.REPORT_NOT_FOUND));
        report.update(request);

        return new ReportResponse.ReportId(report.getId());
    }

    @Override
    @Transactional
    public ReportResponse.ReportId resignReport(Member loginMember, Long reportId) {
        Member member = memberService.findMember(loginMember.getId());

        Report report = reportRepository.findById(reportId).orElseThrow(()
                -> new RestApiException(ReportErrorCode.REPORT_NOT_FOUND));

        if (report.getMember().getId() == loginMember.getId()) {
            report.delete();
        }

        return new ReportResponse.ReportId(report.getId());
    }
}
