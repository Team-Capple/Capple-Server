package com.server.capple.domain.board.mapper;

import com.server.capple.domain.board.entity.Board;
import com.server.capple.domain.board.entity.BoardHeart;
import com.server.capple.domain.member.entity.Member;
import org.springframework.stereotype.Component;


@Component
public class BoardHeartMapper {
    public BoardHeart toBoardHeart(Board board, Member member) {
        return BoardHeart.builder()
                .board(board)
                .member(member)
                .isLiked(false)
                .build();
    }
}
