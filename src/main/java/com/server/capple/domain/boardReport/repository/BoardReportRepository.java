package com.server.capple.domain.boardReport.repository;

import com.server.capple.domain.board.entity.Board;
import com.server.capple.domain.boardReport.entity.BoardReport;
import com.server.capple.domain.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface BoardReportRepository extends JpaRepository<BoardReport, Long> {

    Boolean existsByMemberAndBoard(Member member, Board board);

    List<BoardReport> findByMember(Member member);
}
