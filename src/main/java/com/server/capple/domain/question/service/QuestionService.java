package com.server.capple.domain.question.service;
import com.server.capple.domain.question.dto.response.QuestionResponse.MainQuestion;
import com.server.capple.domain.question.entity.Question;
import com.server.capple.domain.question.entity.QuestionStatus;

public interface QuestionService {
    Question findQuestion(Long questionId);
    MainQuestion getMainQuestion(QuestionStatus questionStatus);
}
