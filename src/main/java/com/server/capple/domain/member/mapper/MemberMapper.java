package com.server.capple.domain.member.mapper;

import com.server.capple.domain.member.dto.MemberResponse;
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

    public MemberResponse.EditMemberInfo toEditMemberInfo(Long memberId, String nickname, String profileImage){
        return MemberResponse.EditMemberInfo.builder()
                .MemberId(memberId)
                .nickname(nickname)
                .profileImage(profileImage)
                .build();
    };
}
