package com.server.capple.domain.board.repository;

import com.server.capple.domain.board.entity.Board;
import com.server.capple.domain.board.entity.BoardHeart;
import com.server.capple.domain.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BoardHeartRepository extends JpaRepository<BoardHeart, Long> {
    Optional<BoardHeart> findByMemberAndBoard(Member member, Board board);

}
