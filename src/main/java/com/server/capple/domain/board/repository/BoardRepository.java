package com.server.capple.domain.board.repository;

import com.server.capple.domain.board.dao.BoardInfoInterface;
import com.server.capple.domain.board.entity.Board;
import com.server.capple.domain.board.entity.BoardType;
import com.server.capple.domain.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface BoardRepository extends JpaRepository<Board, Long> {

    @Query("SELECT b AS board, " +
            "(CASE WHEN bh.member = :member AND bh.isLiked = TRUE THEN TRUE ELSE FALSE END) AS isLike, " +
            "(CASE WHEN b.writer = :member THEN TRUE ELSE FALSE END) AS isMine " +
            "FROM Board b " +
            "LEFT JOIN BoardHeart bh ON b = bh.board " +
            "WHERE :boardType IS NULL OR b.boardType = :boardType ORDER BY b.createdAt DESC")
    List<BoardInfoInterface> findBoardInfosByMemberAndBoardType(Member member, BoardType boardType);

    @Query("SELECT b AS board, " +
            "(CASE WHEN bh.member = :member AND bh.isLiked = TRUE THEN TRUE ELSE FALSE END) AS isLike, " +
            "(CASE WHEN b.writer = :member THEN TRUE ELSE FALSE END) AS isMine " +
            "FROM Board b " +
            "LEFT JOIN BoardHeart bh ON b = bh.board " +
            "WHERE b.content LIKE %:keyword% AND b.boardType = 0 ORDER BY b.createdAt DESC") //FREETYPE = 0
    List<BoardInfoInterface> findBoardInfosByMemberAndKeyword(Member member, String keyword);

    List<Board> findBoardsByBoardType(BoardType boardType);
}
