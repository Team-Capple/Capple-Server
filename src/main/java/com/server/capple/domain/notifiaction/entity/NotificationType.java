package com.server.capple.domain.notifiaction.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum NotificationType {
    BOARD_HEART("누군가 내 게시글을 좋아했어요", null),
    BOARD_COMMENT("누군가 내 게시글에 댓글을 달았어요", null),
    BOARD_COMMENT_DUPLICATE("누군가 같은 게시글에 댓글을 달았어요", null),
    BOARD_COMMENT_HEART("누군가 내 댓글을 좋아했어요", null),
    TODAY_QUESTION_PUBLISHED("오늘의 질문 준비 완료!", "오늘의 질문이 준비 되었어요.\n지금 바로 답변해보세요."),
    TODAY_QUESTION_CLOSED("오늘의 질문 마감 알림", "오늘의 질문 답변 시간이 마감되었어요.\n다른 러너들은 어떻게 답 했는지 확인해보세요."),
    ;
    private final String title;
    private final String content;
}
