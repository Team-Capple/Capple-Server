package com.server.capple.domain.boardCommentReport.repository;

import com.server.capple.domain.boardComment.entity.BoardComment;
import com.server.capple.domain.boardCommentReport.entity.BoardCommentReport;
import com.server.capple.domain.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BoardCommentReportRepository extends JpaRepository<BoardCommentReport, Long> {
    Boolean existsByBoardCommentAndMember(BoardComment boardComment, Member member);
}
