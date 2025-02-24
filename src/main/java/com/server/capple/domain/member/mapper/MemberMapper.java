package com.server.capple.domain.member.mapper;

import com.server.capple.domain.member.dto.MemberResponse;
import com.server.capple.domain.member.entity.AcademyGeneration;
import com.server.capple.domain.member.entity.Member;
import com.server.capple.domain.member.entity.Role;
import org.springframework.stereotype.Component;

@Component
public class MemberMapper {

    public MemberResponse.MyPageMemberInfo toMyPageMemberInfo(String nickname, String profileImage, String joinDate){
        return MemberResponse.MyPageMemberInfo.builder()
                .nickname(nickname)
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

    public Member createMember(String sub, String email, String nickName, Role role, String profileImage, AcademyGeneration academyGeneration) {
        return Member.builder()
            .sub(sub)
            .nickname(nickName)
            .email(email)
            .role(role)
            .profileImage(profileImage)
            .academyGeneration(academyGeneration)
            .build();
    }

    public MemberResponse.MemberId toMemberId(Member member) {
        return MemberResponse.MemberId.builder()
                .memberId(member.getId())
                .build();
    }
}
