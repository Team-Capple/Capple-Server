package com.server.capple.domain.question.service;

import com.server.capple.domain.question.dto.request.QuestionRequest.QuestionCreate;
import com.server.capple.domain.question.dto.response.QuestionResponse.QuestionId;
import com.server.capple.domain.question.entity.Question;

public interface AdminQuestionService {

    QuestionId createQuestion(QuestionCreate request);

    QuestionId deleteQuestion(Long questionId);

    Question setLiveQuestion();
    Question closeLiveQuestion();

}
