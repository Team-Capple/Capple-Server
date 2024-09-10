package com.server.capple.domain.boardCommentReport.service;

import com.server.capple.domain.boardCommentReport.dto.BoardCommentReportResponse;
import com.server.capple.domain.boardCommentReport.entity.BoardCommentReportType;
import com.server.capple.domain.member.entity.Member;

public interface BoardCommentReportService {
    BoardCommentReportResponse.BoardCommentReportCreate createBoardCommentReport(Member member, Long boardCommentId, BoardCommentReportType boardCommentReportType);
}
