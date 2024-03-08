package com.server.capple.domain.member.service;

import com.server.capple.domain.member.dto.MemberRequest;
import com.server.capple.domain.member.dto.MemberResponse;
import com.server.capple.support.ServiceTestConfig;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;


import static org.junit.jupiter.api.Assertions.assertEquals;

@DisplayName("Member 서비스의 ")
@SpringBootTest
public class MemberServiceTest extends ServiceTestConfig {

    @Autowired MemberService memberService;

    @Test
    @DisplayName("Member 프로필 조회 테스트")
    @Transactional
    public void getMyPageMemberInfo() {
        //given & when
        MemberResponse.MyPageMemberInfo memberInfo = memberService.getMemberInfo(member.getId());

        //then
        assertEquals(memberInfo.getNickname(), member.getNickname());
        assertEquals(memberInfo.getEmail(), member.getEmail());
        assertEquals(memberInfo.getProfileImage(), member.getProfileImage());
    }

    @Test
    @DisplayName("프로필 수정 테스트")
    @Transactional
    public void editProfileTest() {
        //given
        MemberRequest.EditMemberInfo request = MemberRequest.EditMemberInfo.builder()
                .nickname("아리")
                .profileImage("ari.png")
                .build();

        //when
        MemberResponse.EditMemberInfo memberInfo = memberService.editMemberInfo(member.getId(), request);

        //then
        assertEquals(memberInfo.getNickname(), "아리");
        assertEquals(memberInfo.getProfileImage(), "ari.png");
    }
}
