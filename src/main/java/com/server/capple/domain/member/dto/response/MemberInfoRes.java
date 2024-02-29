package com.server.capple.domain.member.dto.response;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class MemberInfoRes {
    private String nickname;
    private String email;
    private String profileImage;
    private String joinDate;
}
