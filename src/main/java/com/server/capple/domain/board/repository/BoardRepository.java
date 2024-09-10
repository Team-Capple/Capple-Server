package com.server.capple.domain.board.repository;

import com.server.capple.domain.board.entity.Board;
import com.server.capple.domain.board.entity.BoardType;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface BoardRepository extends JpaRepository<Board, Long> {

    List<Board> findBoardsByBoardType(BoardType boardType);

    @Query("SELECT b FROM Board b WHERE b.content LIKE %:keyword%")
    List<Board> findBoardsByKeyword(String keyword);
}
