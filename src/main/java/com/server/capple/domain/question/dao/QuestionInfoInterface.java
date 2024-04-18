package com.server.capple.domain.question.dao;

import com.server.capple.domain.question.entity.Question;

import java.util.Optional;

public interface QuestionInfoInterface {
    Question getQuestion();
    Boolean getIsAnsweredByMember();
}
