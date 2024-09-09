package com.server.capple.domain.boardReport.mapper;

import com.server.capple.domain.board.entity.Board;
import com.server.capple.domain.boardReport.dto.BoardReportResponse;
import com.server.capple.domain.boardReport.entity.BoardReport;
import com.server.capple.domain.boardReport.entity.BoardReportType;
import com.server.capple.domain.member.entity.Member;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class BoardReportMapper {

    public BoardReport toBoardReport(Member member, Board board, BoardReportType boardReportType) {
        return BoardReport.builder()
                .member(member)
                .board(board)
                .boardReportType(boardReportType)
                .build();
    }

    public BoardReportResponse.BoardReportCreate toBoardReportCreate(BoardReport boardReport) {
        return BoardReportResponse.BoardReportCreate.builder()
                .boardReportId(boardReport.getId())
                .build();
    }

    public BoardReportResponse.BoardReportsGet toBoardReportsGet(List<BoardReportResponse.BoardReportInfo> boardReportInfos) {
        return BoardReportResponse.BoardReportsGet.builder()
                .boardReportInfos(boardReportInfos)
                .build();
    }

    public BoardReportResponse.BoardReportInfo toBoardReportInfo(BoardReport boardReport) {
        return BoardReportResponse.BoardReportInfo.builder()
                .boardReportId(boardReport.getId())
                .reporterId(boardReport.getMember().getId())
                .boardId(boardReport.getBoard().getId())
                .boardReportType(boardReport.getBoardReportType())
                .build();
    }

    public BoardReportResponse.BoardReportUpdate toBoardReportUpdate(BoardReport boardReport) {
        return BoardReportResponse.BoardReportUpdate.builder()
                .boardReportId(boardReport.getId())
                .build();
    }

    public BoardReportResponse.BoardReportResign toBoardReportResign(BoardReport boardReport) {
        return BoardReportResponse.BoardReportResign.builder()
                .boardReportId(boardReport.getId())
                .build();
    }
}
