package com.server.capple.domain.board.repository;

import com.server.capple.domain.board.dao.BoardInfoInterface;
import com.server.capple.domain.board.entity.Board;
import com.server.capple.domain.board.entity.BoardType;
import com.server.capple.domain.member.entity.Member;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;

public interface BoardRepository extends JpaRepository<Board, Long> {

    @Query("SELECT DISTINCT b AS board, " +
            "(CASE WHEN bh.isLiked = TRUE THEN TRUE ELSE FALSE END) AS isLike, " +
            "(CASE WHEN b.writer = :member THEN TRUE ELSE FALSE END) AS isMine " +
            "FROM Board b " +
            "LEFT JOIN BoardHeart bh ON b = bh.board AND bh.member = :member " +
            "WHERE b.createdAt <= :thresholdDate AND (:boardType IS NULL OR b.boardType = :boardType)")
    Slice<BoardInfoInterface> findBoardInfosByMemberAndBoardTypeAndCreatedAtBefore(Member member, BoardType boardType, LocalDateTime thresholdDate, Pageable pageable);

    @Query("SELECT DISTINCT b AS board, " +
            "(CASE WHEN bh.isLiked = TRUE THEN TRUE ELSE FALSE END) AS isLike, " +
            "(CASE WHEN b.writer = :member THEN TRUE ELSE FALSE END) AS isMine " +
            "FROM Board b " +
            "LEFT JOIN BoardHeart bh ON b = bh.board AND bh.member = :member " +
            "WHERE b.createdAt <= :thresholdDate AND b.content LIKE %:keyword% AND b.boardType = 0") //FREETYPE = 0
    Slice<BoardInfoInterface> findBoardInfosByMemberAndKeywordAndCreatedAtBefore(Member member, String keyword, LocalDateTime thresholdDate, Pageable pageable);


    //redis 성능 테스트용
    @Query("SELECT DISTINCT b AS board, " +
            "(CASE WHEN b.writer = :member THEN TRUE ELSE FALSE END) AS isMine " +
            "FROM Board b " +
            "WHERE b.createdAt <= :thresholdDate AND (:boardType IS NULL OR b.boardType = :boardType)")
    Slice<BoardInfoInterface> findBoardInfosForRedisAndCreatedAtBefore(Member member, BoardType boardType, LocalDateTime thresholdDate, Pageable pageable);

}
