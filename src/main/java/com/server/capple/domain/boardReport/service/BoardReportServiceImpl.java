package com.server.capple.domain.boardReport.service;

import com.server.capple.domain.board.entity.Board;
import com.server.capple.domain.board.service.BoardService;
import com.server.capple.domain.boardReport.dto.BoardReportResponse;
import com.server.capple.domain.boardReport.entity.BoardReport;
import com.server.capple.domain.boardReport.entity.BoardReportType;
import com.server.capple.domain.boardReport.mapper.BoardReportMapper;
import com.server.capple.domain.boardReport.repository.BoardReportRepository;
import com.server.capple.domain.member.entity.Member;
import com.server.capple.global.exception.RestApiException;
import com.server.capple.global.exception.errorCode.BoardReportErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class BoardReportServiceImpl implements BoardReportService {

    private final BoardReportRepository boardReportRepository;
    private final BoardService boardService;
    private final BoardReportMapper boardReportMapper;

    @Override
    public BoardReport findBoardReport(Long boardReportId) {
        return boardReportRepository.findById(boardReportId).orElseThrow(()
                -> new RestApiException(BoardReportErrorCode.BOARD_REPORT_NOT_FOUND));
    }

    @Override
    public BoardReportResponse.BoardReportsGet getMyBoardReports(Member member) {

        List<BoardReport> boardReports = boardReportRepository.findByMember(member);

        return boardReportMapper.toBoardReportsGet(boardReports
                .stream()
                .map(boardReportMapper::toBoardReportInfo)
                .toList());
    }

    @Override
    @Transactional
    public BoardReportResponse.BoardReportCreate createBoardReport(Member member, Long boardId, BoardReportType boardReportType) {

        Board board = boardService.findBoard(boardId);

        if (boardReportRepository.existsByMemberAndBoard(member, board)) {
            throw new RestApiException(BoardReportErrorCode.BOARD_REPORT_ALREADY_EXIST);
        }
        BoardReport boardReport = boardReportRepository.save(boardReportMapper.toBoardReport(member, board, boardReportType));
        board.submitReport();

        return boardReportMapper.toBoardReportCreate(boardReport);
    }

    @Override
    @Transactional
    public BoardReportResponse.BoardReportUpdate updateBoardReport(Member member, Long boardReportId, BoardReportType boardReportType) {

        BoardReport boardReport = findBoardReport(boardReportId);

        // 만약 본인이 작성자가 아니면
        if (isReporter(member.getId(), boardReport.getMember().getId())) {
            boardReport.updateBoardReportType(boardReportType);
        } else {
             throw new RestApiException(BoardReportErrorCode.BOARD_REPORT_NO_AUTHORIZATION);
        }

        return boardReportMapper.toBoardReportUpdate(boardReport);
    }

    @Override
    @Transactional
    public BoardReportResponse.BoardReportResign resignBoardReport(Member member, Long boardReportId) {

        BoardReport boardReport = findBoardReport(boardReportId);

        // 만약 본인이 작성자가 아니면
        if (isReporter(member.getId(), boardReport.getMember().getId())) {
            boardReport.delete();
            boardReport.getBoard().cancelReport();
        } else {
            throw new RestApiException(BoardReportErrorCode.BOARD_REPORT_NO_AUTHORIZATION);
        }

        return boardReportMapper.toBoardReportResign(boardReport);
    }

    private Boolean isReporter(Long reporterId, Long memberId) {
        return reporterId.equals(memberId);
    }

}
