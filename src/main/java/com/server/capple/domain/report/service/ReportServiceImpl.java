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
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
}
