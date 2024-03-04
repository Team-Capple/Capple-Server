package com.server.capple.domain.member.dto.response;

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
}
