package com.server.capple.domain.boardReport.service;

import com.server.capple.domain.board.entity.BoardType;
import com.server.capple.domain.boardReport.dto.BoardReportRequest;
import com.server.capple.domain.boardReport.dto.BoardReportResponse;
import com.server.capple.domain.boardReport.entity.BoardReportType;
import com.server.capple.domain.member.entity.Member;

public interface BoardReportService {
    BoardReportResponse.BoardReportsGet getBoardReports(Member member);

    BoardReportResponse.BoardReportCreate createReport(Member member, BoardReportType boardReportType);

    BoardReportResponse.BoardReportUpdate updateBoardReport(Member member, Long reportId, BoardReportType boardReportType);

    BoardReportResponse.BoardReportResign resignBoardReport(Member member, Long boardReportId);

}
