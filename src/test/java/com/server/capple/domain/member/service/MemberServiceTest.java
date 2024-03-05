package com.server.capple.domain.member.service;

import com.server.capple.domain.member.dto.response.MemberResponse;
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
}
