package com.server.capple.domain.notifiaction.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum NotificationType {
    BOARD_HEART("누군가 내 게시글에 좋아요를 눌렀어요", null),
    BOARD_COMMENT("누군가 내 게시글에 댓글을 달았어요", null),
    BOARD_COMMENT_DUPLICATE("누군가 댓글을 달았어요", null),
    BOARD_COMMENT_HEART("누군가 내 댓글에 좋아요를 눌렀어요", null),
    TODAY_QUESTION_PUBLISHED("오늘의 질문 준비 완료!", null),
    TODAY_QUESTION_CLOSED("오늘의 질문 답변 마감!", null),
    LIVE_QUESTION_ANSWER_ADDED("오늘의 질문", "새로운 답변이 달렸어요!"),
    NEW_FREE_BOARD("자유 게시판", "새로운 게시글이 올라왔어요!"),
    ANSWER_HEART("누군가 내 답변에 좋아요를 눌렀어요", null),
    ANSWER_COMMENT("누군가 내 답변에 댓글을 달았어요", null),
    ANSWER_COMMENT_DUPLICATE("누군가 답변에 댓글을 달았어요", null),
    ANSWER_COMMENT_HEART("누군가 내 답변의 댓글에 좋아요를 눌렀어요", null),
    ;
    private final String title;
    private final String body;
}
