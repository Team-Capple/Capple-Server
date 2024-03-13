package com.server.capple.domain.question.service;
import com.server.capple.config.security.AuthMember;
import com.server.capple.domain.member.entity.Member;
import com.server.capple.domain.question.dto.response.QuestionResponse.QuestionSummary;
import com.server.capple.domain.question.dto.response.QuestionResponse.QuestionInfos;
import com.server.capple.domain.question.entity.Question;

public interface QuestionService {
    Question findQuestion(Long questionId);
    QuestionSummary getMainQuestion();

    QuestionInfos getQuestions(Member member);
}
