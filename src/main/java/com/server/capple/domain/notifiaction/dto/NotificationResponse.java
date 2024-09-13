package com.server.capple.domain.notifiaction.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

public class NotificationResponse {
    @Getter
    @Builder
    @ToString
    @AllArgsConstructor
    public static class NotificationInfo {
        private String title;
        private String subtitle;
        private String content;
        private String boardId;
        private String boardCommentId;
    }
}
