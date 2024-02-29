package com.server.capple.domain.mapper;

import com.server.capple.domain.member.dto.response.MemberInfoRes;
import org.springframework.stereotype.Component;

@Component
public class MemberMapper {

    public MemberInfoRes toMemberInfoRes(String nickname, String email, String profileImage, String joinDate){
        return MemberInfoRes.builder()
                .nickname(nickname)
                .email(email)
                .profileImage(profileImage)
                .joinDate(joinDate)
                .build();
    }
}
