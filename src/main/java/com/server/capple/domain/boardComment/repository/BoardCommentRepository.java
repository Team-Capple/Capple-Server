package com.server.capple.domain.boardComment.repository;

import com.server.capple.domain.boardComment.dao.BoardCommentInfoInterface;
import com.server.capple.domain.boardComment.entity.BoardComment;
import com.server.capple.domain.member.entity.Member;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface BoardCommentRepository extends JpaRepository<BoardComment, Long> {
    @Query("SELECT bc AS boardComment, " +
            "(CASE WHEN bch.isLiked = TRUE THEN TRUE ELSE FALSE END) AS isLike, " +
            "(CASE WHEN bc.writer = :member THEN TRUE ELSE FALSE END) AS isMine," +
            "bc.writer.academyGeneration AS writerAcademyGeneration " +
            "FROM BoardComment bc " +
            "LEFT JOIN BoardCommentHeart bch ON bc = bch.boardComment AND bch.member = :member " +
            "WHERE bc.board.id = :boardId AND (bc.id > :lastIndex OR :lastIndex IS NULL)")
    Slice<BoardCommentInfoInterface> findBoardCommentInfosByMemberAndBoardIdAndIdIsGreaterThan(Member member, Long boardId, Long lastIndex, Pageable pageable);
}
