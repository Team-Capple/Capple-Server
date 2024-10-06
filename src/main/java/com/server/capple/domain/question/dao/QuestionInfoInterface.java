package com.server.capple.domain.question.dao;

import com.server.capple.domain.question.entity.Question;

public interface QuestionInfoInterface {
    Question getQuestion();
    Boolean getIsAnsweredByMember();
}
