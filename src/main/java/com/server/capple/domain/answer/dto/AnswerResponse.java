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
        private Long answerId;
        private Long writerId;
        private String profileImage;
        private String nickname;
        private String content;
        private Boolean isMine;
        private Boolean isReported;
        private Boolean isLiked;
        private String writeAt;
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
        private Long answerId;
        private Long writerId;
        private String nickname;
        private String profileImage;
        private String content;
        private int heartCount;
        private String writeAt;
        private Boolean isLiked;
    }

    @Getter
    @AllArgsConstructor
    public static class MemberAnswerList {
        private List<MemberAnswerInfo> memberAnswerInfos;
    }
}
