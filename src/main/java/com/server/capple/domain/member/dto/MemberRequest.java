package com.server.capple.domain.member.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;


public class MemberRequest {

    @Getter
    @Builder
    @AllArgsConstructor
    public static class editMemberInfo {
        @Size(min=2, max=20, message="닉네임은 2~20자로 입력해주세요.")
        @NotBlank(message="닉네임을 입력해주세요.")
        private String nickname;
        private String profileImage;
    }

}
