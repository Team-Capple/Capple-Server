package com.server.capple.domain.boardReport.mapper;

import com.server.capple.domain.board.entity.Board;
import com.server.capple.domain.boardReport.dto.BoardReportResponse;
import com.server.capple.domain.boardReport.entity.BoardReport;
import com.server.capple.domain.boardReport.entity.BoardReportType;
import com.server.capple.domain.member.entity.Member;
import org.springframework.stereotype.Component;

@Component
public class BoardReportMapper {

    public BoardReport toBoardReport(Member member, Board board, BoardReportType boardReportType) {
        return BoardReport.builder()
                .member(member)
                .board(board)
                .reportType(boardReportType)
                .build();
    }

    public BoardReportResponse.BoardReportCreate toBoardReportCreate(BoardReport boardReport) {
        return BoardReportResponse.BoardReportCreate.builder()
                .boardReportId(boardReport.getId())
                .build();
    }
}
