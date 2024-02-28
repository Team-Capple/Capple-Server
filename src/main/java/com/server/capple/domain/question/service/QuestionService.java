package com.server.capple.domain.question.service;

import com.server.capple.domain.question.entity.Question;

public interface QuestionService {
    Question findQuestion(Long questionId);
}
