package com.server.capple.domain.question.entity;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum QuestionStatus {

    LAST("지난 질문"),
    ONGOING("진행중인 질문");
    private final String toKorean;
}
