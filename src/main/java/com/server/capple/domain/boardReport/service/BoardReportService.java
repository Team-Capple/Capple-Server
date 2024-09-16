package com.server.capple.domain.boardReport.service;

import com.server.capple.domain.boardReport.dto.BoardReportResponse;
import com.server.capple.domain.boardReport.entity.BoardReport;
import com.server.capple.domain.boardReport.entity.BoardReportType;
import com.server.capple.domain.member.entity.Member;

public interface BoardReportService {

    BoardReport findBoardReport(Long boardReportId);

    BoardReportResponse.BoardReportsGet getMyBoardReports(Member member);

    BoardReportResponse.BoardReportCreate createBoardReport(Member member, Long boardId, BoardReportType boardReportType);

    BoardReportResponse.BoardReportUpdate updateBoardReport(Member member, Long reportId, BoardReportType boardReportType);

    BoardReportResponse.BoardReportResign resignBoardReport(Member member, Long boardReportId);
}
