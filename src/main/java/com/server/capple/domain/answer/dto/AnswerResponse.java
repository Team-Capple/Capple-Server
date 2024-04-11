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
        private Boolean isMyAnswer;
        private Boolean isReported;
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


    @Getter
    @Builder
    public static class MemberAnswerInfo {
        private Long questionId;
        private String nickname;
        private String profileImage;
        private String content;
        private String tags;
        private int heartCount;
        private String writeAt;
    }

    @Getter
    @AllArgsConstructor
    public static class MemberAnswerList {
        private List<MemberAnswerInfo> memberAnswerInfos;
    }
}
