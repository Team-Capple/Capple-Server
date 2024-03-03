package com.server.capple.domain.answer.dto;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;

public class AnswerResponse {
    @Getter
    @AllArgsConstructor
    public static class AnswerId {
        private Long answerId;
    }

    @Data
    @Builder
    public static class AnswerInfo {
        private String profileImage;
        private String nickname;
        private String content;
        private String tags;
    }

    @Data
    @Builder
    public static class AnswerList {
        private List<AnswerInfo> answerInfos;
    }
}
