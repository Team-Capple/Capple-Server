package com.server.capple.domain.boardReport.service;

import com.server.capple.domain.board.entity.Board;
import com.server.capple.domain.board.service.BoardService;
import com.server.capple.domain.boardReport.dto.BoardReportResponse;
import com.server.capple.domain.boardReport.entity.BoardReport;
import com.server.capple.domain.boardReport.entity.BoardReportType;
import com.server.capple.domain.boardReport.mapper.BoardReportMapper;
import com.server.capple.domain.boardReport.repository.BoardReportRepository;
import com.server.capple.domain.member.entity.Member;
import com.server.capple.domain.member.service.MemberService;
import com.server.capple.global.exception.RestApiException;
import com.server.capple.global.exception.errorCode.BoardReportErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class BoardReportServiceImpl implements BoardReportService {

    private final BoardReportRepository boardReportRepository;
    private final MemberService memberService;
    private final BoardService boardService;
    private final BoardReportMapper boardReportMapper;

    @Override
    public BoardReport findBoardReport(Long boardReportId) {
        return boardReportRepository.findById(boardReportId).orElseThrow(()
                -> new RestApiException(BoardReportErrorCode.BOARD_REPORT_NOT_FOUND));
    }

    @Override
    public BoardReportResponse.BoardReportsGet getMyBoardReports(Member member) {
        Member findMember = memberService.findMember(member.getId());

        List<BoardReport> boardReports = boardReportRepository.findByMember(member);

        return boardReportMapper.toBoardReportsGet(boardReports
                .stream()
                .map(boardReportMapper::toBoardReportInfo)
                .toList());
    }

    @Override
    public BoardReportResponse.BoardReportCreate createBoardReport(Member member, Long boardId, BoardReportType boardReportType) {
        Member reportMember = memberService.findMember(member.getId());

        Board board = boardService.findBoard(boardId);

        if (boardReportRepository.existsByMemberAndBoard(reportMember, board)) {
            throw new RestApiException(BoardReportErrorCode.BOARD_REPORT_ALREADY_EXIST);
        }
        BoardReport boardReport = boardReportRepository.save(boardReportMapper.toBoardReport(reportMember, board, boardReportType));

        return boardReportMapper.toBoardReportCreate(boardReport);
    }

//    @Override
//    public BoardReportResponse.BoardReportUpdate updateBoardReport(Member member, Long reportId, BoardReportType boardReportType) {
//        return null;
//    }
//
//    @Override
//    public BoardReportResponse.BoardReportResign resignBoardReport(Member member, Long boardReportId) {
//        return null;
//    }
}
