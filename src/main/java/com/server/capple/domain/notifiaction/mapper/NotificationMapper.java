package com.server.capple.domain.notifiaction.mapper;

import com.server.capple.config.apns.dto.ApnsClientRequest;
import com.server.capple.domain.notifiaction.dto.NotificationResponse.NotificationInfo;
import com.server.capple.domain.notifiaction.entity.Notification;
import org.springframework.stereotype.Component;

@Component
public class NotificationMapper {
    public Notification toNotification(Long memberId, ApnsClientRequest.BoardNotificationBody boardNotificationBody) {
        return Notification.builder()
            .memberId(memberId)
            .title(boardNotificationBody.getAps().getAlert().getTitle())
            .content(boardNotificationBody.getAps().getAlert().getBody())
            .boardId(boardNotificationBody.getBoardId())
            .build();
    }

    public Notification toNotification(Long memberId, ApnsClientRequest.BoardCommentNotificationBody boardCommentNotificationBody) {
        return Notification.builder()
            .memberId(memberId)
            .title(boardCommentNotificationBody.getAps().getAlert().getTitle())
            .subtitle(boardCommentNotificationBody.getAps().getAlert().getSubtitle())
            .content(boardCommentNotificationBody.getAps().getAlert().getBody())
            .boardId(boardCommentNotificationBody.getBoardId())
            .boardCommentId(boardCommentNotificationBody.getBoardCommentId())
            .build();
    }
}
