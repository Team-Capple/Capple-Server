package com.server.capple.domain.question.service;

import com.server.capple.domain.question.dto.request.QuestionRequest.QuestionCreate;
import com.server.capple.domain.question.dto.response.QuestionResponse.QuestionId;

public interface AdminQuestionService {

    QuestionId createQuestion(QuestionCreate request);

    QuestionId deleteQuestion(Long questionId);

    QuestionId setLiveQuestion();
    QuestionId closeLiveQuestion();

    void savePopularTags(Long questionId);

}
