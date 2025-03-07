package com.server.capple.domain.board.repository;

import com.server.capple.domain.board.dao.BoardInfoInterface;
import com.server.capple.domain.board.entity.Board;
import com.server.capple.domain.board.entity.BoardType;
import com.server.capple.domain.member.entity.Member;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface BoardRepository extends JpaRepository<Board, Long> {

    @Query("SELECT DISTINCT b AS board, " +
            "(CASE WHEN bh.isLiked = TRUE THEN TRUE ELSE FALSE END) AS isLike, " +
            "(CASE WHEN b.writer = :member THEN TRUE ELSE FALSE END) AS isMine, " +
            "b.writer.nickname AS writerNickname, " +
            "b.writer.academyGeneration AS writerAcademyGeneration " +
            "FROM Board b " +
            "LEFT JOIN BoardHeart bh ON b = bh.board AND bh.member = :member " +
            "WHERE (:boardType IS NULL OR b.boardType = :boardType) AND (b.id < :lastIndex OR :lastIndex IS NULL)")
    Slice<BoardInfoInterface> findBoardInfosByMemberAndBoardTypeAndIdIsLessThan(Member member, BoardType boardType, Long lastIndex, Pageable pageable);

    @Query("SELECT DISTINCT b AS board, " +
            "(CASE WHEN bh.isLiked = TRUE THEN TRUE ELSE FALSE END) AS isLike, " +
            "(CASE WHEN b.writer = :member THEN TRUE ELSE FALSE END) AS isMine, " +
            "b.writer.nickname AS writerNickname, " +
            "b.writer.academyGeneration AS writerAcademyGeneration " +
            "FROM Board b " +
            "LEFT JOIN BoardHeart bh ON b = bh.board AND bh.member = :member " +
            "WHERE (b.id < :lastIndex OR :lastIndex IS NULL) AND b.content LIKE %:keyword% AND b.boardType = 0") //FREETYPE = 0
    Slice<BoardInfoInterface> findBoardInfosByMemberAndKeywordAndIdIsLessThan(Member member, String keyword, Long lastIndex, Pageable pageable);


    @Query("SELECT b AS board, " +
            "(CASE WHEN bh.isLiked = TRUE THEN TRUE ELSE FALSE END) AS isLike, " +
            "(CASE WHEN b.writer = :member THEN TRUE ELSE FALSE END) AS isMine, " +
            "b.writer.nickname AS writerNickname, " +
            "b.writer.academyGeneration AS writerAcademyGeneration " +
            "FROM Board b " +
            "LEFT JOIN BoardHeart bh ON b = bh.board AND bh.member = :member " +
            "WHERE b.id = :boardId")
    BoardInfoInterface findBoardByMember(Member member, Long boardId);

    //redis 성능 테스트용
    @Query("SELECT DISTINCT b AS board, " +
            "(CASE WHEN b.writer = :member THEN TRUE ELSE FALSE END) AS isMine, " +
            "b.writer.nickname AS writerNickname, " +
            "b.writer.academyGeneration AS writerAcademyGeneration " +
            "FROM Board b " +
            "WHERE (:boardType IS NULL OR b.boardType = :boardType) AND (b.id < :lastIndex OR :lastIndex IS NULL)")
    Slice<BoardInfoInterface> findBoardInfosForRedisAndIdIsLessThan(Member member, BoardType boardType, Long lastIndex, Pageable pageable);

    @Query("SELECT COUNT(b) FROM Board b")
    Integer getBoardCount();
}
