package com.server.capple.domain.boardCommentReport.service;

import com.server.capple.domain.boardComment.entity.BoardComment;
import com.server.capple.domain.boardComment.mapper.BoardCommentMapper;
import com.server.capple.domain.boardComment.service.BoardCommentService;
import com.server.capple.domain.boardCommentReport.dto.BoardCommentReportResponse;
import com.server.capple.domain.boardCommentReport.entity.BoardCommentReport;
import com.server.capple.domain.boardCommentReport.entity.BoardCommentReportType;
import com.server.capple.domain.boardCommentReport.mapper.BoardCommentReportMapper;
import com.server.capple.domain.boardCommentReport.repository.BoardCommentReportRepository;
import com.server.capple.domain.member.entity.Member;
import com.server.capple.global.exception.RestApiException;
import com.server.capple.global.exception.errorCode.BoardCommentReportErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class BoardCommentReportServiceImpl implements BoardCommentReportService {
    private final BoardCommentReportRepository boardCommentReportRepository;
    private final BoardCommentReportMapper boardCommentReportMapper;
    private final BoardCommentService boardCommentService;

    @Override
    @Transactional
    public BoardCommentReportResponse.BoardCommentReportCreate createBoardCommentReport(Member member, Long boardCommentId, BoardCommentReportType boardCommentReportType) {
        // 신고 대상
        BoardComment boardComment = boardCommentService.findBoardComment(boardCommentId);

        // 신고 여부 확인
        if(boardCommentReportRepository.existsByBoardCommentAndMember(boardComment, member)) {
            throw new RestApiException(BoardCommentReportErrorCode.COMMENT_REPORT_ALREADY_EXIST);
        }

        // 신고
        BoardCommentReport newReport = boardCommentReportMapper.toBoardCommentReportEntity(member, boardComment, boardCommentReportType);
        boardCommentReportRepository.save(newReport);

        return new BoardCommentReportResponse.BoardCommentReportCreate(newReport.getId());
    }
}
