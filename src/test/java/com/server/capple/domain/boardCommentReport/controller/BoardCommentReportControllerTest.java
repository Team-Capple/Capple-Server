package com.server.capple.domain.boardCommentReport.controller;

import com.server.capple.domain.boardCommentReport.dto.BoardCommentReportRequest;
import com.server.capple.domain.boardCommentReport.dto.BoardCommentReportResponse;
import com.server.capple.domain.boardCommentReport.entity.BoardCommentReportType;
import com.server.capple.domain.boardCommentReport.service.BoardCommentReportService;
import com.server.capple.domain.member.entity.Member;
import com.server.capple.support.ControllerTestConfig;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.ResultActions;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@DisplayName("BoardCommentReport 컨트롤러의")
@SpringBootTest
@AutoConfigureMockMvc
public class BoardCommentReportControllerTest extends ControllerTestConfig {

    @MockBean
    private BoardCommentReportService boardCommentReportService;

    @Test
    @DisplayName("게시글 댓글 신고 API 테스트")
    public void updateBoardCommentTest() throws Exception {
        //given
        final String url = "/reports/board-comment";

        BoardCommentReportRequest.BoardCommentReportCreate request = new BoardCommentReportRequest.BoardCommentReportCreate(1L, BoardCommentReportType.COMMENT_INADEQUATE_BOARD_CHARACTER);
        BoardCommentReportResponse.BoardCommentReportCreate response = new BoardCommentReportResponse.BoardCommentReportCreate(1L);
        doReturn(response).when(boardCommentReportService).createBoardCommentReport(any(Member.class), any(Long.class), any(BoardCommentReportType.class));

        //when
        ResultActions resultActions = this.mockMvc.perform(
                post(url)
                .contentType(APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(request))
                .header("Authorization", "Bearer " + jwt));

        //then
        resultActions.
                andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("COMMON200"))
                .andExpect(jsonPath("$.message").value("요청에 성공하였습니다."));
    }

}
