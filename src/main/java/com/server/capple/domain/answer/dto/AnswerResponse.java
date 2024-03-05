package com.server.capple.domain.answer.dto;

import java.util.List;

import lombok.*;

public class AnswerResponse {
    @Getter
    @AllArgsConstructor
    public static class AnswerId {
        private Long answerId;
    }

    @Getter
    @AllArgsConstructor
    @Builder
    public static class AnswerInfo {
        private String profileImage;
        private String nickname;
        private String content;
        private String tags;
    }

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class AnswerList {
        private List<AnswerInfo> answerInfos;
    }

    @Getter
    @AllArgsConstructor
    public static class AnswerLike {
        private Long answerId;
        private Boolean isLiked;
    }
}
