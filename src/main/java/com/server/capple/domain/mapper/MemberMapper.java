package com.server.capple.domain.mapper;

import com.server.capple.domain.member.dto.response.MemberResponse;
import org.springframework.stereotype.Component;

@Component
public class MemberMapper {

    public MemberResponse.MyPageMemberInfo toMyPageMemberInfo(String nickname, String email, String profileImage, String joinDate){
        return MemberResponse.MyPageMemberInfo.builder()
                .nickname(nickname)
                .email(email)
                .profileImage(profileImage)
                .joinDate(joinDate)
                .build();
    }
}
