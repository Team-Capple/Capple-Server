package com.server.capple.domain.answer.controller;

import com.server.capple.domain.answer.dto.AnswerRequest;
import com.server.capple.domain.answer.dto.AnswerResponse;
import com.server.capple.domain.answer.dto.AnswerResponse.MemberAnswerInfo;
import com.server.capple.domain.answer.service.AnswerService;
import com.server.capple.domain.member.entity.Member;
import com.server.capple.global.common.SliceResponse;
import com.server.capple.support.ControllerTestConfig;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doReturn;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@DisplayName("Answer 컨트롤러의")
@SpringBootTest
@AutoConfigureMockMvc
public class AnswerControllerTest extends ControllerTestConfig {
    @MockBean
    private AnswerService answerService;

    @Test
    @DisplayName("답변 생성 API 테스트")
    public void createAnswerTest() throws Exception {
        //given
        final String url = "/answers/question/{questionId}";

        AnswerRequest request = getAnswerRequest();
        AnswerResponse.AnswerId response = new AnswerResponse.AnswerId(1L);

        doReturn(response).when(answerService).createAnswer(any(Member.class), any(Long.class), any(AnswerRequest.class));

        //when
        ResultActions resultActions = this.mockMvc.perform(post(url, question.getId())
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(request))
                .header("Authorization", "Bearer " + jwt)
                );

        //then
        resultActions.andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.code").value("COMMON200"))
                .andExpect(jsonPath("$.message").value("요청에 성공하였습니다."))
                .andExpect(jsonPath("$.result.answerId").value(1L));
    }

    @Test
    @DisplayName("답변 수정 API 테스트")
    public void updateAnswerTest() throws Exception {
        //given
        final String url = "/answers/{answerId}";

        AnswerRequest request = getAnswerRequest();
        AnswerResponse.AnswerId response = new AnswerResponse.AnswerId(1L);

        doReturn(response).when(answerService).updateAnswer(any(Member.class), any(Long.class), any(AnswerRequest.class));

        //when
        ResultActions resultActions = this.mockMvc.perform(patch(url, answer.getId())
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(request))
                .header("Authorization", "Bearer " + jwt)
        );

        //then
        resultActions.andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.code").value("COMMON200"))
                .andExpect(jsonPath("$.message").value("요청에 성공하였습니다."))
                .andExpect(jsonPath("$.result.answerId").value(1L));
    }

    @Test
    @DisplayName("답변 삭제 API 테스트")
    public void deleteAnswerTest() throws Exception {
        //given
        final String url = "/answers/{answerId}";

        AnswerResponse.AnswerId response = new AnswerResponse.AnswerId(1L);

        doReturn(response).when(answerService).deleteAnswer(any(Member.class), any(Long.class));

        //when
        ResultActions resultActions = this.mockMvc.perform(delete(url, answer.getId())
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .header("Authorization", "Bearer " + jwt)
        );

        //then
        resultActions.andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.code").value("COMMON200"))
                .andExpect(jsonPath("$.message").value("요청에 성공하였습니다."))
                .andExpect(jsonPath("$.result.answerId").value(1L));
    }

    @Test
    @DisplayName("Answer 좋아요/취소 테스트")
    public void toggleAnswerHeartTest() throws Exception {
        //given
        final String url = "/answers/{answerId}/heart";

        AnswerResponse.AnswerLike response = new AnswerResponse.AnswerLike(1L, Boolean.TRUE);

        doReturn(response).when(answerService).toggleAnswerHeart(any(Member.class), any(Long.class));

        //when
        ResultActions resultActions = this.mockMvc.perform(post(url, answer.getId())
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .header("Authorization", "Bearer " + jwt)
        );

        //then
        resultActions.andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.code").value("COMMON200"))
                .andExpect(jsonPath("$.message").value("요청에 성공하였습니다."))
                .andExpect(jsonPath("$.result.answerId").value(1L))
                .andExpect(jsonPath("$.result.isLiked").value(Boolean.TRUE));
    }

    @Test
    @DisplayName("작성한 답변 목록 조회 API 테스트")
    public void getMyPageMemberAnswerTest() throws Exception {
        //given
        final String url = "/answers";
        SliceResponse<MemberAnswerInfo> response = getSliceMemberAnswerInfos();
        given(answerService.getMemberAnswer(any(Member.class), any(PageRequest.class))).willReturn(response);

        //when
        ResultActions resultActions = mockMvc.perform(get(url).accept(MediaType.APPLICATION_JSON)
                .header("Authorization", "Bearer " + jwt)
        );

        //then
        resultActions.
                andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("COMMON200"))
                .andExpect(jsonPath("$.message").value("요청에 성공하였습니다."))
//                .andExpect(jsonPath("$.result.memberAnswerInfos[0].nickname").value("루시"))
                .andExpect(jsonPath("$.result.content[0].content").value("나는 무자비한 사람이 좋아"));
    }

    @Test
    @DisplayName("좋아한 답변 목록 조회 API 테스트")
    public void getMyPageMemberHeartAnswerTest() throws Exception {
        //given
        final String url = "/answers/heart";
        AnswerResponse.MemberAnswerList response = getMemberAnswerList();
        given(answerService.getMemberHeartAnswer(any(Member.class))).willReturn(response);

        //when
        ResultActions resultActions = mockMvc.perform(get(url).accept(MediaType.APPLICATION_JSON)
                .header("Authorization", "Bearer " + jwt)
        );

        //then
        resultActions.
                andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("COMMON200"))
                .andExpect(jsonPath("$.message").value("요청에 성공하였습니다."))
                .andExpect(jsonPath("$.result.memberAnswerInfos[0].heartCount").value(1))
                .andExpect(jsonPath("$.result.memberAnswerInfos[0].answerId").value(1));
    }
}