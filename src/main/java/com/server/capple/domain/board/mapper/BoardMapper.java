package com.server.capple.domain.board.mapper;

import com.server.capple.domain.board.dto.BoardRequest;
import com.server.capple.domain.board.dto.BoardResponse;
import com.server.capple.domain.board.entity.Board;
import com.server.capple.domain.board.entity.BoardType;
import com.server.capple.domain.member.entity.Member;
import org.springframework.stereotype.Component;

@Component
public class BoardMapper {

    public Board toBoard(
            Member member,
            BoardType boardType,
            String content,
            Integer heartCount,
            Integer commentCount
    ) {
        return Board.builder()
                .member(member)
                .boardType(boardType)
                .content(content)
                .heartCount(heartCount)
                .commentCount(commentCount)
                .build();
    }

    public BoardResponse.BoardCreate toBoardCreate(
            Board board
            ) {
        return BoardResponse.BoardCreate.builder()
                .boardId(board.getId())
                .build();
    }
}
