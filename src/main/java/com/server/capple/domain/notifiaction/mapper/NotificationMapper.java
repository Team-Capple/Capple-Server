package com.server.capple.domain.notifiaction.mapper;

import com.server.capple.domain.board.entity.Board;
import com.server.capple.domain.boardComment.entity.BoardComment;
import com.server.capple.domain.notifiaction.dto.NotificationResponse.NotificationInfo;
import com.server.capple.domain.notifiaction.entity.Notification;
import com.server.capple.domain.notifiaction.entity.NotificationType;
import com.server.capple.domain.question.entity.Question;
import com.server.capple.global.common.SliceResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class NotificationMapper {
    public Notification toNotification(Long memberId, Board board, NotificationType type) {
        return Notification.builder()
            .memberId(memberId)
            .board(board)
            .type(type)
            .build();
    }

    public Notification toNotification(Long memberId, Board board, BoardComment boardComment, NotificationType type) {
        return Notification.builder()
            .memberId(memberId)
            .board(board)
            .boardComment(boardComment)
            .type(type)
            .build();
    }

    public Notification toNotification(Question question, NotificationType type) {
        return Notification.builder()
            .question(question)
            .type(type)
            .build();
    }

    public SliceResponse<NotificationInfo> toNotificationInfoSlice(Slice<Notification> notification) {
        return SliceResponse.toSliceResponse(notification, notification.stream().map(this::toNotificationInfo).toList());
    }

    private NotificationInfo toNotificationInfo(Notification notification) {
        return switch (notification.getType()) {
            case BOARD_HEART -> toBoardNotificationInfo(notification);
            case BOARD_COMMENT, BOARD_COMMENT_DUPLICATE, BOARD_COMMENT_HEART ->
                toBoardCommentNotificationInfo(notification);
            case TODAY_QUESTION_PUBLISHED, TODAY_QUESTION_CLOSED -> toQuestionNotificationInfo(notification);
        };
    }

    private NotificationInfo toBoardNotificationInfo(Notification notification) {
        return NotificationInfo.builder()
            .title(notification.getType().getTitle())
            .content(notification.getBoard().getContent())
            .boardId(notification.getBoard().getId().toString())
            .createdAt(notification.getCreatedAt())
            .build();
    }

    private NotificationInfo toBoardCommentNotificationInfo(Notification notification) {
        return NotificationInfo.builder()
            .title(notification.getType().getTitle())
            .subtitle(notification.getBoardComment().getContent())
            .content(notification.getBoard().getContent())
            .boardId(notification.getBoard().getId().toString())
            .boardCommentId(notification.getBoardComment().getId().toString())
            .createdAt(notification.getCreatedAt())
            .build();
    }

    private NotificationInfo toQuestionNotificationInfo(Notification notification) {
        return NotificationInfo.builder()
            .title(notification.getType().getTitle())
            .subtitle(notification.getType().getContent())
            .content(notification.getQuestion().getContent())
            .questionId(notification.getQuestion().getId().toString())
            .createdAt(notification.getCreatedAt())
            .build();
    }
}
