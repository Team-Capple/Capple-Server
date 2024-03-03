package com.server.capple.domain.question.mapper;

import com.server.capple.domain.question.dto.request.QuestionRequest.QuestionCreate;
import com.server.capple.domain.question.dto.response.QuestionResponse.QuestionId;
import com.server.capple.domain.question.dto.response.QuestionResponse.QuestionSummary;
import com.server.capple.domain.question.entity.Question;
import org.springframework.stereotype.Component;

@Component
public class QuestionMapper {

    public Question toQuestion(QuestionCreate request) {
        return Question.builder()
                .questionStatus(request.getQuestionStatus())
                .content(request.getContent())
                .build();
    }

    public QuestionId toQuestionId(Question question) {
        return QuestionId.builder()
                .questionId(question.getId())
                .build();
    }

    public QuestionSummary toQuestionSummary(Question question) {
        return QuestionSummary.builder()
                .questionId(question.getId())
                .questionStatus(question.getQuestionStatus())
                .content(question.getContent())
                .build();
    }
}