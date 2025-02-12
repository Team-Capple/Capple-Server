package com.server.capple.config.apns.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.server.capple.domain.board.entity.Board;
import com.server.capple.domain.boardComment.entity.BoardComment;
import com.server.capple.domain.notifiaction.entity.NotificationType;
import com.server.capple.domain.question.entity.Question;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

public class ApnsClientRequest {
    @Getter
    @NoArgsConstructor
    @ToString
    public static class SimplePushBody {
        private Aps aps;
        private String boardId;
        private String boardCommentId;
        @Builder
        public SimplePushBody(String title, String subTitle, String body, Integer badge, String sound, String threadId, String targetContentId, String boardId, String boardCommentId) {
            this.aps = new Aps(new Aps.Alert(title, subTitle, body), badge, sound, threadId, targetContentId);
            this.boardId = boardId;
            this.boardCommentId = boardCommentId;
        }

        @ToString
        @Getter
        @AllArgsConstructor
        @NoArgsConstructor
        public static class Aps {
            private Alert alert;
            private Integer badge;
            @Schema(defaultValue = "default")
            private String sound; // Library/Sounds 폴더 내의 파일 이름
            @JsonProperty("thread-id")
            private String threadId;
            @JsonProperty("target-content-id")
            private String targetContentId; // 프론트 측 작업 필요함
            @ToString
            @Getter
            @AllArgsConstructor
            @NoArgsConstructor
            public static class Alert {
                private String title;
                private String subtitle;
                private String body;
            }
        }
    }

    @Getter
    @NoArgsConstructor
    @ToString
    public static class BoardNotificationBody {
        private Aps aps;
        private String boardId;
        @Builder
        public BoardNotificationBody(NotificationType type, Board board) {
            this.aps = Aps.builder()
                .threadId("board-" + board.getId())
                .alert(Aps.Alert.builder()
                    .title(type.getTitle())
                    .body(board.getContent())
                    .build())
                .build();
            this.boardId = board.getId().toString();
        }

        @ToString
        @Getter
        @Builder
        @AllArgsConstructor
        @NoArgsConstructor
        public static class Aps {
            private Alert alert;
            @Builder.Default
            private String sound = "default"; // Library/Sounds 폴더 내의 파일 이름
            @JsonProperty("thread-id")
            private String threadId;

            @ToString
            @Getter
            @Builder
            @AllArgsConstructor
            @NoArgsConstructor
            public static class Alert {
                private String title;
                private String body;
            }
        }
    }

    @Getter
    @NoArgsConstructor
    @ToString
    public static class BoardCommentNotificationBody {
        private Aps aps;
        private String boardId;
        private String boardCommentId;
        @Builder
        public BoardCommentNotificationBody(NotificationType type, Board board, BoardComment boardComment) {
            this.aps = Aps.builder().threadId("board-" + board.getId())
                .alert(Aps.Alert.builder()
                    .title(type.getTitle())
                    .body(boardComment.getContent())
                    .build())
                .build();
            this.boardId = board.getId().toString();
            this.boardCommentId = boardComment.getId().toString();
        }

        @ToString
        @Getter
        @Builder
        @AllArgsConstructor
        @NoArgsConstructor
        public static class Aps {
            private Alert alert;
            private Integer badge;
            @Builder.Default
            private String sound = "default"; // Library/Sounds 폴더 내의 파일 이름
            @JsonProperty("thread-id")
            private String threadId;

            @ToString
            @Getter
            @Builder
            @AllArgsConstructor
            @NoArgsConstructor
            public static class Alert {
                private String title;
                private String subtitle;
                private String body;
            }
        }
    }

    @Getter
    @NoArgsConstructor
    @ToString
    public static class QuestionNotificationBody {
        private Aps aps;
        private String questionId;
        @Builder
        public QuestionNotificationBody(NotificationType type, Question question) {
            this.aps = Aps.builder().threadId("question")
                .alert(Aps.Alert.builder()
                    .title(type.getTitle())
                    .body(question.getContent())
                    .build())
                .build();
            this.questionId = question.getId().toString();
        }

        @ToString
        @Getter
        @Builder
        @AllArgsConstructor
        @NoArgsConstructor
        public static class Aps {
            private Alert alert;
            private Integer badge;
            @Builder.Default
            private String sound = "default"; // Library/Sounds 폴더 내의 파일 이름
            @JsonProperty("thread-id")
            private String threadId;

            @ToString
            @Getter
            @Builder
            @AllArgsConstructor
            @NoArgsConstructor
            public static class Alert {
                private String title;
                private String subtitle;
                private String body;
            }
        }
    }
}
