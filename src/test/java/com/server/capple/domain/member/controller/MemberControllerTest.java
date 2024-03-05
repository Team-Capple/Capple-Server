package com.server.capple.domain.member.controller;

import com.server.capple.domain.member.dto.response.MemberResponse;
import com.server.capple.domain.member.entity.Member;
import com.server.capple.domain.member.service.MemberService;
import com.server.capple.support.ControllerTestConfig;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@DisplayName("Member 컨트롤러의")
@SpringBootTest
@AutoConfigureMockMvc
public class MemberControllerTest extends ControllerTestConfig {

    @MockBean private MemberService memberService;

    @Test
    @DisplayName("마이페이지 프로필 조회 API 테스트")
    public void getMyPageMemberInfoTest() throws Exception {
        //given
        final String url = "/members/{memberId}";
        final String joinDate = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy.MM.dd")) + " 가입";
        final Long memberId = 1L;

        Member member = createMember();
        MemberResponse.MyPageMemberInfo response = MemberResponse.MyPageMemberInfo.builder()
                .nickname(member.getNickname())
                .email(member.getEmail())
                .profileImage(member.getProfileImage())
                .joinDate(joinDate)
                .build();

        given(memberService.getMemberInfo(any(Long.class))).willReturn(response);

        //when
        ResultActions resultActions = mockMvc.perform(get(url, memberId).accept(MediaType.APPLICATION_JSON));

        //then
        resultActions.
                andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("COMMON200"))
                .andExpect(jsonPath("$.message").value("요청에 성공하였습니다."))
                .andExpect(jsonPath("$.result.nickname").value("루시"))
                .andExpect(jsonPath("$.result.email").value("tnals2384@gmail.com"))
                .andExpect(jsonPath("$.result.profileImage").value("lucy.jpg"))
                .andExpect(jsonPath("$.result.joinDate").value(joinDate));

        verify(memberService).getMemberInfo(memberId);
    }

}
