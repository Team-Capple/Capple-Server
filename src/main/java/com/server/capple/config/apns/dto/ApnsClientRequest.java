package com.server.capple.config.apns.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

public class ApnsClientRequest {

    @Getter
    @NoArgsConstructor
    @ToString
    public static class SimplePushBody {
        private Aps aps;

        public SimplePushBody(String title, String subTitle, String body, Integer badge, String threaId, String targetContentId) {
            this.aps = new Aps(new Aps.Alert(title, subTitle, body), badge, threaId, targetContentId);
        }

        @ToString
        @Getter
        @AllArgsConstructor
        @NoArgsConstructor
        public static class Aps {
            private Alert alert;
            private Integer badge;
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
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    @ToString
    public static class FullAlertBody {
        private Aps aps;

        @Getter
        @AllArgsConstructor
        @NoArgsConstructor
        @Builder
        @ToString
        public static class Aps {
            private Alert alert; // alert 정보
            private Integer badge; // 앱 아이콘에 표시할 뱃지 숫자
            @Schema(defaultValue = "default")
            private String sound; // Library/Sounds 폴더 내의 파일 이름
            @Schema(defaultValue = "thread-id")
            private String threadId; // 알림 그룹화를 위한 thread id (UNNotificationContent 객체의 threadIdentifier와 일치해야 함)
            private String category; // 알림 그룹화를 위한 category, (UNNotificationCategory 식별자와 일치해야 함)
            @Schema(defaultValue = "0")
            @JsonProperty("content-available")
            private Integer contentAvailable; // 백그라운드 알림 여부, 1이면 백그라운드 알림, 0이면 포그라운드 알림 (백그라운드일 경우 alert, badge, sound는 넣으면 안됨)
            @Schema(defaultValue = "0")
            @JsonProperty("mutable-content")
            private Integer mutableContent; // 알림 서비스 확장 플래그
            @Schema(defaultValue = "")
            @JsonProperty("target-content-id")
            private String targetContentId; // 알림이 클릭되었을 때 가져올 창의 식별자, UNNotificationContent 객체에 채워짐

            @Getter
            @AllArgsConstructor
            @NoArgsConstructor
            @Builder
            @ToString
            public static class Alert {
                @Schema(defaultValue = "title")
                private String title;
                @Schema(defaultValue = "subTitle")
                private String subtitle;
                @Schema(defaultValue = "body")
                private String body;
                @Schema(defaultValue = "")
                @JsonProperty("launch-image")
                private String launchImage; // 실행시 보여줄 이미지 파일, 기본 실행 이미지 대신 입력한 이미지 또는 스토리보드가 켜짐
            }
        }
    }

}
