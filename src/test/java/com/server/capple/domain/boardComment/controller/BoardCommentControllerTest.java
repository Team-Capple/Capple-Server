package com.server.capple.domain.boardComment.controller;

import com.server.capple.domain.boardComment.dto.BoardCommentRequest;
import com.server.capple.domain.boardComment.service.BoardCommentService;
import com.server.capple.domain.member.entity.Member;
import com.server.capple.support.ControllerTestConfig;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.ResultActions;

import static com.server.capple.domain.boardComment.dto.BoardCommentResponse.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.when;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@DisplayName("BoardComment 컨트롤러의")
@SpringBootTest
@AutoConfigureMockMvc
public class BoardCommentControllerTest extends ControllerTestConfig {

    @MockBean
    private BoardCommentService boardCommentService;

    @Test
    @DisplayName("게시글 댓글 생성 API 테스트")
    public void createBoardCommentTest() throws Exception {
        //given
        final String url = "/boardComments/board/{boardId}";

        BoardCommentRequest request = getBoardCommentRequest();
        BoardCommentId response = new BoardCommentId(1L);

        when(boardCommentService.createBoardComment(any(Member.class), any(Long.class), any(BoardCommentRequest.class)))
                .thenReturn(response);

        //when
        ResultActions resultActions = this.mockMvc.perform(post(url, 1L)
                .contentType(APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(request))
                .header("Authorization", "Bearer " + jwt));

        //then
        resultActions.andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.code").value("COMMON200"))
                .andExpect(jsonPath("$.message").value("요청에 성공하였습니다."))
                .andExpect(jsonPath("$.result.boardCommentId").value(1L));
    }

    @Test
    @DisplayName("게시글 댓글 수정 API 테스트")
    public void updateBoardCommentTest() throws Exception {
        //given
        final String url = "/boardComments/{commentId}";

        BoardCommentRequest request = getBoardCommentRequest();
        BoardCommentId response = new BoardCommentId(1L);

        doReturn(response).when(boardCommentService).updateBoardComment(any(Member.class), any(Long.class), any(BoardCommentRequest.class));

        //when
        ResultActions resultActions = this.mockMvc.perform(patch(url, 1L)
                .contentType(APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(request))
                .header("Authorization", "Bearer " + jwt));

        //then
        resultActions.
                andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("COMMON200"))
                .andExpect(jsonPath("$.message").value("요청에 성공하였습니다."))
                .andExpect(jsonPath("$.result.boardCommentId").value(1L));
    }

    @Test
    @DisplayName("게시글 댓글 삭제 API 테스트")
    public void deleteBoardCommentTest() throws Exception {
        //given
        final String url = "/boardComments/{commentId}";
        BoardCommentId response = new BoardCommentId(1L);

        doReturn(response).when(boardCommentService).deleteBoardComment(any(Member.class), any(Long.class));

        //when
        ResultActions resultActions = this.mockMvc.perform(delete(url, 1L)
                .contentType(APPLICATION_JSON_VALUE)
                .header("Authorization", "Bearer " + jwt));

        //then
        resultActions.
                andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("COMMON200"))
                .andExpect(jsonPath("$.message").value("요청에 성공하였습니다."))
                .andExpect(jsonPath("$.result.boardCommentId").value(1L));
    }

    @Test
    @DisplayName("게시글 댓글 좋아요/취소 API 테스트")
    public void heartBoardCommentTest() throws Exception {
        //given
        final String url = "/boardComments/heart/{commentId}";
        BoardCommentHeart response = new BoardCommentHeart(1L, Boolean.TRUE);

        doReturn(response).when(boardCommentService).heartBoardComment(any(Member.class), any(Long.class));

        //when
        ResultActions resultActions = this.mockMvc.perform(patch(url, 1L)
                .contentType(APPLICATION_JSON_VALUE)
                .header("Authorization", "Bearer " + jwt));

        //then
        resultActions.
                andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("COMMON200"))
                .andExpect(jsonPath("$.message").value("요청에 성공하였습니다."))
                .andExpect(jsonPath("$.result.boardCommentId").value(1L))
                .andExpect(jsonPath("$.result.isLiked").value(Boolean.TRUE));
    }

    @Test
    @DisplayName("게시글 댓글 리스트 조회 API 테스트")
    public void getBoardCommentInfosTest() throws Exception {
        //given
        final String url = "/boardComments/{boardId}";
        BoardCommentInfos response = getBoardCommentInfos();

        doReturn(response).when(boardCommentService).getBoardCommentInfos(any(Member.class), any(Long.class));

        //when
        ResultActions resultActions = this.mockMvc.perform(get(url, 1L)
                .contentType(APPLICATION_JSON_VALUE)
                .header("Authorization", "Bearer " + jwt));

        //then
        resultActions.
                andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("COMMON200"))
                .andExpect(jsonPath("$.message").value("요청에 성공하였습니다."))
                .andExpect(jsonPath("$.result.boardCommentInfos[0].boardCommentId").value(1L))
//                .andExpect(jsonPath("$.result.boardCommentInfos[0].writer").value("루시"))
                .andExpect(jsonPath("$.result.boardCommentInfos[0].content").value("댓글"))
                .andExpect(jsonPath("$.result.boardCommentInfos[0].heartCount").value(2L))
                .andExpect(jsonPath("$.result.boardCommentInfos[0].isLiked").value(true));
    }
}