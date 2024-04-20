package com.server.capple.domain.member.controller;

import com.server.capple.domain.member.dto.MemberRequest;
import com.server.capple.domain.member.dto.MemberResponse;
import com.server.capple.domain.member.entity.Member;
import com.server.capple.domain.member.service.MemberService;
import com.server.capple.support.ControllerTestConfig;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Disabled;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.ResultActions;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@DisplayName("Member 컨트롤러의")
@SpringBootTest
@AutoConfigureMockMvc
public class MemberControllerTest extends ControllerTestConfig {

    @MockBean private MemberService memberService;

    @Disabled
    @Test
    @DisplayName("프로필 조회 API 테스트")
    public void getMyPageMemberInfoTest() throws Exception {
        //given
        final String url = "/members/mypage";
        final String joinDate = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy.MM.dd")) + " 가입";
        final Long memberId = 1L;

        Member member = createMember();
        MemberResponse.MyPageMemberInfo response = MemberResponse.MyPageMemberInfo.builder()
                .nickname(member.getNickname())
                .profileImage(member.getProfileImage())
                .joinDate(joinDate)
                .build();

        given(memberService.getMemberInfo(any(Member.class))).willReturn(response);

        //when
        ResultActions resultActions = mockMvc.perform(get(url, memberId).accept(MediaType.APPLICATION_JSON)
            .header("Authorization", "Bearer " + jwt)
        );

        //then
        resultActions.
                andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("COMMON200"))
                .andExpect(jsonPath("$.message").value("요청에 성공하였습니다."))
                .andExpect(jsonPath("$.result.nickname").value("루시"))
                .andExpect(jsonPath("$.result.profileImage").value("https://owori.s3.ap-northeast-2.amazonaws.com/story/capple_default_image_10635d7a-5f8c-4af2-b062-9a9420634eb3.png"))
                .andExpect(jsonPath("$.result.joinDate").value(joinDate));

        verify(memberService).getMemberInfo(member);
    }

    @Test
    @DisplayName("프로필 이미지 업로드 API 테스트")
    public void uploadImageTest() throws Exception {
        //given
        final String url = "/members/image";

        MockMultipartFile image = new MockMultipartFile("image", "image.png", "png", "image".getBytes(StandardCharsets.UTF_8));
        MemberResponse.ProfileImage response = new MemberResponse.ProfileImage("lucy.png");
        given(memberService.uploadImage(any())).willReturn(response);

        //when
        ResultActions resultActions = mockMvc.perform(
                multipart(HttpMethod.POST, url)
                        .file(image)
                        .accept(MediaType.APPLICATION_JSON)
                .header("Authorization", "Bearer " + jwt)
        );

        //then
        resultActions.
                andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("COMMON200"))
                .andExpect(jsonPath("$.message").value("요청에 성공하였습니다."))
                .andExpect(jsonPath("$.result.imageUrl").value(response.getImageUrl()));

        verify(memberService).uploadImage(image);
    }

    @Test
    @DisplayName("프로필 수정 API 테스트")
    public void editMemberInfoTest() throws Exception {
        //given
        final String url = "/members/mypage";
        final Long memberId = 1L;

        MemberRequest.EditMemberInfo request = MemberRequest.EditMemberInfo.builder()
                .profileImage("ari.png")
                .nickname("아리")
                .build();

        MemberResponse.EditMemberInfo response = MemberResponse.EditMemberInfo.builder()
                .MemberId(memberId)
                .nickname("아리")
                .profileImage("ari.png")
                .build();

        given(memberService.editMemberInfo(any(Member.class), any(MemberRequest.EditMemberInfo.class))).willReturn(response);

        //when
        ResultActions resultActions = mockMvc.perform(post(url, memberId)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(request))
                .header("Authorization", "Bearer " + jwt)
        );

        //then
        resultActions.
                andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("COMMON200"))
                .andExpect(jsonPath("$.message").value("요청에 성공하였습니다."))
                .andExpect(jsonPath("$.result.memberId").value(memberId))
                .andExpect(jsonPath("$.result.nickname").value("아리"))
                .andExpect(jsonPath("$.result.profileImage").value("ari.png"));
    }

}
