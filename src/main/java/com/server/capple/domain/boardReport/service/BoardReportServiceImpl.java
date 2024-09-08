package com.server.capple.domain.boardReport.service;

import com.server.capple.domain.board.entity.BoardType;
import com.server.capple.domain.boardReport.dto.BoardReportRequest;
import com.server.capple.domain.boardReport.dto.BoardReportResponse;
import com.server.capple.domain.boardReport.entity.BoardReportType;
import com.server.capple.domain.member.entity.Member;
import org.springframework.stereotype.Service;

@Service
public class BoardReportServiceImpl implements BoardReportService{
    @Override
    public BoardReportResponse.BoardReportsGet getBoardReports(Member member) {
        return null;
    }

    @Override
    public BoardReportResponse.BoardReportCreate createReport(Member member, BoardReportType boardReportType) {
        return null;
    }

    @Override
    public BoardReportResponse.BoardReportUpdate updateBoardReport(Member member, Long reportId, BoardReportType boardReportType) {
        return null;
    }

    @Override
    public BoardReportResponse.BoardReportResign resignBoardReport(Member member, Long boardReportId) {
        return null;
    }
}
