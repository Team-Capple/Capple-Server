package com.server.capple.domain.boardComment.repository;

import com.server.capple.domain.boardComment.dao.BoardCommentInfoInterface;
import com.server.capple.domain.boardComment.entity.BoardComment;
import com.server.capple.domain.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface BoardCommentRepository extends JpaRepository<BoardComment, Long> {
    @Query("SELECT bc AS boardComment, (CASE WHEN bch.member = :member AND bch.isLiked = TRUE THEN TRUE ELSE FALSE END) AS isLike, " +
            "(CASE WHEN bc.writer = :member THEN TRUE ELSE FALSE END) AS isMine " +
            "FROM BoardComment bc LEFT JOIN BoardCommentHeart bch ON bc = bch.boardComment " +
            "WHERE bc.board.id = :boardId ORDER BY bc.createdAt DESC")
    List<BoardCommentInfoInterface> findBoardCommentInfosByMemberAndBoardId(Member member, Long boardId);
}
