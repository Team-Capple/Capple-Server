package com.server.capple.domain.board.repository;

import com.server.capple.domain.board.dao.BoardInfoInterface;
import com.server.capple.domain.board.entity.Board;
import com.server.capple.domain.board.entity.BoardType;
import com.server.capple.domain.member.entity.Member;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface BoardRepository extends JpaRepository<Board, Long> {

    @Query("SELECT DISTINCT b AS board, " +
            "(CASE WHEN bh.isLiked = TRUE THEN TRUE ELSE FALSE END) AS isLike, " +
            "(CASE WHEN b.writer = :member THEN TRUE ELSE FALSE END) AS isMine " +
            "FROM Board b " +
            "LEFT JOIN BoardHeart bh ON b = bh.board AND bh.member = :member " +
            "WHERE :boardType IS NULL OR b.boardType = :boardType")
    Slice<BoardInfoInterface> findBoardInfosByMemberAndBoardType(Member member, BoardType boardType, Pageable pageable);

    @Query("SELECT DISTINCT b AS board, " +
            "(CASE WHEN bh.isLiked = TRUE THEN TRUE ELSE FALSE END) AS isLike, " +
            "(CASE WHEN b.writer = :member THEN TRUE ELSE FALSE END) AS isMine " +
            "FROM Board b " +
            "LEFT JOIN BoardHeart bh ON b = bh.board AND bh.member = :member " +
            "WHERE b.content LIKE %:keyword% AND b.boardType = 0 ORDER BY b.createdAt DESC") //FREETYPE = 0
    Slice<BoardInfoInterface> findBoardInfosByMemberAndKeyword(Member member, String keyword, Pageable pageable);


    //redis 성능 테스트용
    @Query("SELECT DISTINCT b AS board, " +
            "(CASE WHEN b.writer = :member THEN TRUE ELSE FALSE END) AS isMine " +
            "FROM Board b " +
            "WHERE :boardType IS NULL OR b.boardType = :boardType ORDER BY b.createdAt DESC")
    Slice<BoardInfoInterface> findBoardInfosForRedis(Member member, BoardType boardType, Pageable pageable);

    @Query("SELECT COUNT(b) FROM Board b")
    Integer getBoardCount();
}
