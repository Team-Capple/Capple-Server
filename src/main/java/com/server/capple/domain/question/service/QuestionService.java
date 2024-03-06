package com.server.capple.domain.question.service;
import com.server.capple.domain.question.dto.response.QuestionResponse.QuestionInfo;
import com.server.capple.domain.question.dto.response.QuestionResponse.QuestionInfos;
import com.server.capple.domain.question.entity.Question;
import com.server.capple.domain.question.entity.QuestionStatus;

public interface QuestionService {
    Question findQuestion(Long questionId);
    QuestionInfo getMainQuestion();

    QuestionInfos getQuestions();
}
