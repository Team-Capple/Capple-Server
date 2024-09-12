package com.server.capple.domain.boardComment.repository;

import com.server.capple.domain.boardComment.entity.BoardComment;
import com.server.capple.domain.boardComment.entity.BoardCommentHeart;
import com.server.capple.domain.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BoardCommentHeartRepository extends JpaRepository<BoardCommentHeart,Long> {
    Optional<BoardCommentHeart> findByMemberAndBoardComment(Member member, BoardComment boardComment);

}
