package com.server.capple.domain.boardCommentReport.mapper;

import com.server.capple.domain.boardComment.entity.BoardComment;
import com.server.capple.domain.boardCommentReport.entity.BoardCommentReport;
import com.server.capple.domain.boardCommentReport.entity.BoardCommentReportType;
import com.server.capple.domain.member.entity.Member;
import org.springframework.stereotype.Component;

@Component
public class BoardCommentReportMapper {

    public BoardCommentReport toBoardCommentReportEntity(Member member, BoardComment boardComment, BoardCommentReportType boardCommentReportType) {
        return BoardCommentReport.builder()
                .member(member)
                .boardComment(boardComment)
                .boardCommentReportType(boardCommentReportType)
                .build();
    }

}
