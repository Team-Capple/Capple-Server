package com.server.capple.domain.member.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;


public class MemberResponse {

    @Getter
    @Builder
    public static class MyPageMemberInfo {
        private String nickname;
        private String email;
        private String profileImage;
        private String joinDate;
    }

    @Getter
    @Builder
    public static class editMemberInfo {
        private Long MemberId;
        private String nickname;
        private String profileImage;
    }

    @AllArgsConstructor
    @Getter
    public static class ProfileImage {
        private String imageUrl;
    }
}
