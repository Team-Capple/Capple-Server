package com.server.capple.domain.notifiaction.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum NotificationType {
    BOARD_HEART("누군가 내 게시글에 좋아요를 눌렀어요"),
    BOARD_COMMENT("누군가 내 게시글에 댓글을 달았어요"),
    BOARD_COMMENT_DUPLICATE("누군가 댓글을 달았어요"),
    BOARD_COMMENT_HEART("누군가 내 댓글에 좋아요를 눌렀어요"),
    TODAY_QUESTION_PUBLISHED("오늘의 질문 준비 완료!"),
    TODAY_QUESTION_CLOSED("오늘의 질문 답변 마감!"),
    ;
    private final String title;
}
