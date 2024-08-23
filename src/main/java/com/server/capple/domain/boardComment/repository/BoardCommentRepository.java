package com.server.capple.domain.boardComment.repository;

import com.server.capple.domain.boardComment.entity.BoardComment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BoardCommentRepository extends JpaRepository<BoardComment, Long> {
    List<BoardComment> findBoardCommentByBoardIdOrderByCreatedAt(Long boardId);
}
