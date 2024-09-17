package com.server.capple.domain.boardCommentReport.service;

import com.server.capple.domain.boardCommentReport.entity.BoardCommentReport;
import com.server.capple.domain.boardCommentReport.entity.BoardCommentReportType;
import com.server.capple.domain.boardCommentReport.service.BoardCommentReportService;
import com.server.capple.support.ServiceTestConfig;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DisplayName("BoardCommentReport 서비스의 ")
@SpringBootTest
public class BoardCommentReportServiceTest extends ServiceTestConfig {

    @Autowired
    private BoardCommentReportService boardCommentReportService;

    @Test
    @DisplayName("게시글 댓글 신고 테스트")
    @Transactional
    public void createBoardCommentReportTest() {
        //given
        Long boardCommentReportId = 1L;

        //when
        boardCommentReportService.createBoardCommentReport(member, boardComment.getId(), BoardCommentReportType.COMMENT_INADEQUATE_BOARD_CHARACTER);
        BoardCommentReport boardCommentReport = boardCommentReportService.findBoardCommentReport(boardCommentReportId);

        //then
        assertEquals(boardCommentReportId, boardCommentReport.getId());
        assertEquals(boardComment, boardCommentReport.getBoardComment());
        assertEquals(boardComment.getIsReport(), Boolean.TRUE);
        assertEquals(BoardCommentReportType.COMMENT_INADEQUATE_BOARD_CHARACTER, boardCommentReport.getBoardCommentReportType());
    }

}