package com.server.capple.domain.notifiaction.dto;

import com.server.capple.domain.notifiaction.entity.Notification;

public class NotificationDBResponse {
    public interface NotificationDBInfo {
        Notification getNotification();
        Boolean getIsResponsedQuestion();
        Boolean getIsReportedBoard();
    }
}
