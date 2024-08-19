package com.server.capple.domain.board.repository;

import com.server.capple.domain.board.entity.Board;
import com.server.capple.domain.board.entity.BoardType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BoardRepository extends JpaRepository<Board, Long> {

    List<Board> findBoardsByBoardType(BoardType boardType);
}
