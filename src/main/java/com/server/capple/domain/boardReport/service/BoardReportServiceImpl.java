package com.server.capple.domain.boardReport.service;

import com.server.capple.domain.board.entity.BoardType;
import com.server.capple.domain.boardReport.dto.BoardReportRequest;
import com.server.capple.domain.boardReport.dto.BoardReportResponse;
import com.server.capple.domain.boardReport.entity.BoardReport;
import com.server.capple.domain.boardReport.entity.BoardReportType;
import com.server.capple.domain.boardReport.repository.BoardReportRepository;
import com.server.capple.domain.member.entity.Member;
import com.server.capple.domain.report.entity.Report;
import com.server.capple.global.exception.RestApiException;
import com.server.capple.global.exception.errorCode.BoardReportErrorCode;
import com.server.capple.global.exception.errorCode.ReportErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class BoardReportServiceImpl implements BoardReportService {

    private final BoardReportRepository boardReportRepository;

    @Override
    public BoardReport findBoardReport(Long boardReportId) {
        return boardReportRepository.findById(boardReportId).orElseThrow(()
                -> new RestApiException(BoardReportErrorCode.BOARD_REPORT_NOT_FOUND));
    }

    @Override
    public BoardReportResponse.BoardReportsGet getBoardReports(Member member) {
        return null;
    }

    @Override
    public BoardReportResponse.BoardReportCreate createBoardReport(Member member, BoardReportType boardReportType) {
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
