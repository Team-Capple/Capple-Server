package com.server.capple.domain.question.service;

import com.server.capple.domain.member.entity.Member;
import com.server.capple.domain.question.dto.response.QuestionResponse;
import com.server.capple.domain.question.dto.response.QuestionResponse.QuestionInfo;
import com.server.capple.domain.question.dto.response.QuestionResponse.QuestionSummary;
import com.server.capple.domain.question.entity.Question;
import com.server.capple.global.common.SliceResponse;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;

public interface QuestionService {
    Question findQuestion(Long questionId);

    QuestionSummary getMainQuestion(Member member);

    SliceResponse<QuestionInfo> getQuestions(Member member, LocalDateTime lastDateTime, Pageable pageable);

    SliceResponse<QuestionInfo> getAnsweredQuestions(Member member, LocalDateTime lastDateTime, Pageable pageable);

    QuestionResponse.QuestionToggleHeart toggleQuestionHeart(Member member, Long questionId);
}
