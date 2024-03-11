package com.server.capple.domain.member.mapper;

import com.server.capple.domain.member.dto.MemberResponse;
import com.server.capple.domain.member.entity.Member;
import com.server.capple.domain.member.entity.Role;
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

    public MemberResponse.SignInResponse toSignInResponse(String accessToken, String refreshToken, Boolean isMember) {
        return MemberResponse.SignInResponse.builder()
            .accessToken(accessToken)
            .refreshToken(refreshToken)
            .isMember(isMember)
            .build();
    }

    public Member createMember(String sub, String email, String nickName, Role role, String profileImage) {
        return Member.builder()
            .sub(sub)
            .email(email)
            .nickname(nickName)
            .role(role)
            .profileImage(profileImage)
            .build();
    }
}
