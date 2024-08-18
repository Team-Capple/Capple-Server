package com.server.capple.domain.member.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.lang.Nullable;


public class MemberRequest {

    @Getter
    @Builder
    @AllArgsConstructor
    public static class EditMemberInfo {
        @Size(min=2, max=20, message="닉네임은 2~20자로 입력해주세요.")
        @NotBlank(message="닉네임을 입력해주세요.")
        private String nickname;
        private String profileImage;
    }

    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    public static class signUp {
        private String signUpToken;
        private String email;
        private String nickname;
        private String profileImage;
        private String deviceToken;
    }

}
