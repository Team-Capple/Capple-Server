package com.server.capple.domain.question.entity;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum QuestionStatus {

    LIVE("진행중인 질문"),
    OLD("지난 질문"),
    HOLD("보류중인 질문"),
    PENDING("대기중인 질문");
    private final String toKorean;
}
