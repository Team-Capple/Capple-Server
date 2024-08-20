package com.server.capple.domain.answerComment.controller;

import com.server.capple.domain.answerComment.dto.AnswerCommentRequest;
import com.server.capple.domain.answerComment.dto.AnswerCommentResponse;
import com.server.capple.domain.answerComment.service.AnswerCommentService;
import com.server.capple.domain.member.entity.Member;
import com.server.capple.support.ControllerTestConfig;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@DisplayName("AnswerComment 컨트롤러의")
@SpringBootTest
@AutoConfigureMockMvc
public class AnswerCommentControllerTest extends ControllerTestConfig {

    @MockBean
    private AnswerCommentService answerCommentService;

    @Test
    @DisplayName("답변 댓글 생성 API 테스트")
    public void createAnswerCommentTest() throws Exception {
        //given
        final String url = "/answerComments/answer/{answerId}";

        AnswerCommentRequest request = getAnswerCommentRequest();
        AnswerCommentResponse.AnswerCommentId response = new AnswerCommentResponse.AnswerCommentId(1L);

        when(answerCommentService.createAnswerComment(any(Member.class), any(Long.class), any(AnswerCommentRequest.class)))
                .thenReturn(response);

        //when
        ResultActions resultActions = this.mockMvc.perform(post(url, 1L)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(request))
                .header("Authorization", "Bearer " + jwt));

        //then
        resultActions.andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.code").value("COMMON200"))
                .andExpect(jsonPath("$.message").value("요청에 성공하였습니다."))
                .andExpect(jsonPath("$.result.answerCommentId").value(1L));
    }

    @Test
    @DisplayName("답변 댓글 수정 API 테스트")
    public void updateAnswerCommentTest() throws Exception {
        //given
        final String url = "/answerComments/{commentId}";

        AnswerCommentRequest request = getAnswerCommentRequest();
        AnswerCommentResponse.AnswerCommentId response = new AnswerCommentResponse.AnswerCommentId(1L);

        doReturn(response).when(answerCommentService).updateAnswerComment(any(Member.class), any(Long.class), any(AnswerCommentRequest.class));

        //when
        ResultActions resultActions = this.mockMvc.perform(patch(url, 1L)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(request))
                .header("Authorization", "Bearer " + jwt));

        //then
        resultActions.
                andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("COMMON200"))
                .andExpect(jsonPath("$.message").value("요청에 성공하였습니다."))
                .andExpect(jsonPath("$.result.answerCommentId").value(1L));
    }

    @Test
    @DisplayName("답변 댓글 삭제 API 테스트")
    public void deleteAnswerCommentTest() throws Exception {
        //given
        final String url = "/answerComments/{commentId}";
        AnswerCommentResponse.AnswerCommentId response = new AnswerCommentResponse.AnswerCommentId(1L);

        doReturn(response).when(answerCommentService).deleteAnswerComment(any(Member.class), any(Long.class));

        //when
        ResultActions resultActions = this.mockMvc.perform(delete(url, 1L)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .header("Authorization", "Bearer " + jwt));

        //then
        resultActions.
                andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("COMMON200"))
                .andExpect(jsonPath("$.message").value("요청에 성공하였습니다."))
                .andExpect(jsonPath("$.result.answerCommentId").value(1L));
    }

    @Test
    @DisplayName("답변 좋아요/취소 API 테스트")
    public void heartAnswerCommentTest() throws Exception {
        //given
        final String url = "/answerComments/heart/{commentId}";
        AnswerCommentResponse.AnswerCommentHeart response = new AnswerCommentResponse.AnswerCommentHeart(1L, Boolean.TRUE);

        doReturn(response).when(answerCommentService).heartAnswerComment(any(Member.class), any(Long.class));

        //when
        ResultActions resultActions = this.mockMvc.perform(patch(url, 1L)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .header("Authorization", "Bearer " + jwt));

        //then
        resultActions.
                andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("COMMON200"))
                .andExpect(jsonPath("$.message").value("요청에 성공하였습니다."))
                .andExpect(jsonPath("$.result.answerCommentId").value(1L))
                .andExpect(jsonPath("$.result.isLiked").value(Boolean.TRUE));
    }

    @Test
    @DisplayName("답변에 대한 댓글 조회 API 테스트")
    public void getAnswerCommentInfosTest() throws Exception {
        //given
        final String url = "/answerComments/{answerId}";
        AnswerCommentResponse.AnswerCommentInfos response = getAnswerCommentInfos();

        doReturn(response).when(answerCommentService).getAnswerCommentInfos(any(Long.class));

        //when
        ResultActions resultActions = this.mockMvc.perform(get(url, 1L)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .header("Authorization", "Bearer " + jwt));

        //then
        resultActions.
                andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("COMMON200"))
                .andExpect(jsonPath("$.message").value("요청에 성공하였습니다."))
                .andExpect(jsonPath("$.result.answerCommentInfos[0].answerCommentId").value(1L))
                .andExpect(jsonPath("$.result.answerCommentInfos[0].writer").value("루시"))
                .andExpect(jsonPath("$.result.answerCommentInfos[0].content").value("댓글 1"))
                .andExpect(jsonPath("$.result.answerCommentInfos[0].heartCount").value(3L))
                .andExpect(jsonPath("$.result.answerCommentInfos[0].createdAt").value("2022-11-01T12:02:00"));

    }
}
