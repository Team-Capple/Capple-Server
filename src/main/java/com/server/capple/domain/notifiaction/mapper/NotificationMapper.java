package com.server.capple.domain.notifiaction.mapper;

import com.server.capple.domain.board.entity.Board;
import com.server.capple.domain.boardComment.entity.BoardComment;
import com.server.capple.domain.member.entity.Member;
import com.server.capple.domain.notifiaction.dto.NotificationResponse.NotificationInfo;
import com.server.capple.domain.notifiaction.entity.Notification;
import com.server.capple.domain.notifiaction.entity.NotificationLog;
import com.server.capple.domain.notifiaction.entity.NotificationType;
import com.server.capple.domain.question.entity.Question;
import com.server.capple.global.common.SliceResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class NotificationMapper {
    public Notification toNotification(Member member, NotificationLog notificationLog, NotificationType type) {
        return Notification.builder()
            .member(member)
            .notificationLog(notificationLog)
            .type(type)
            .build();
    }

    public Notification toNotification(NotificationLog notificationLog, NotificationType type) {
        return Notification.builder()
            .notificationLog(notificationLog)
            .type(type)
            .build();
    }

    public NotificationLog toNotificationLog(Board board) {
        return NotificationLog.builder()
            .body(board.getContent())
            .boardId(board.getId())
            .build();
    }

    public NotificationLog toNotificationLog(Board board, BoardComment boardComment) {
        return NotificationLog.builder()
            .body(boardComment.getContent())
            .boardId(board.getId())
            .boardCommentId(boardComment.getId())
            .build();
    }

    public NotificationLog toNotificationLog(Question question) {
        return NotificationLog.builder()
            .body(question.getContent())
            .questionId(question.getId())
            .build();
    }

    public SliceResponse<NotificationInfo> toNotificationInfoSlice(Slice<Notification> notification, Long lastIndex) {
        return SliceResponse.toSliceResponse(notification, notification.stream().map(this::toNotificationInfo).toList(), lastIndex.toString(), null);
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
            .content(notification.getNotificationLog().getBody())
            .boardId(notification.getNotificationLog().getBoardId().toString())
            .createdAt(notification.getCreatedAt())
            .build();
    }

    private NotificationInfo toBoardCommentNotificationInfo(Notification notification) {
        return NotificationInfo.builder()
            .title(notification.getType().getTitle())
            .content(notification.getNotificationLog().getBody())
            .boardId(notification.getNotificationLog().getBoardId().toString())
            .boardCommentId(notification.getNotificationLog().getBoardCommentId().toString())
            .createdAt(notification.getCreatedAt())
            .build();
    }

    private NotificationInfo toQuestionNotificationInfo(Notification notification) {
        return NotificationInfo.builder()
            .title(notification.getType().getTitle())
            .content(notification.getNotificationLog().getBody())
            .questionId(notification.getNotificationLog().getQuestionId().toString())
            .createdAt(notification.getCreatedAt())
            .build();
    }
}
