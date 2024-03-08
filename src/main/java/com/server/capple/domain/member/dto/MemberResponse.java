package com.server.capple.domain.member.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.List;


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
    public static class EditMemberInfo {
        private Long MemberId;
        private String nickname;
        private String profileImage;
    }

    @AllArgsConstructor
    @Getter
    public static class ProfileImage {
        private String imageUrl;
    }

    @Getter
    @AllArgsConstructor
    public static class DeleteProfileImages {
        private List<String> DeleteImages;
    }
}
