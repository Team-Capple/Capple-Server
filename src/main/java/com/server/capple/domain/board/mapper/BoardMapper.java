package com.server.capple.domain.board.mapper;

import com.server.capple.domain.board.dto.BoardResponse.BoardInfo;
import com.server.capple.domain.board.entity.Board;
import com.server.capple.domain.board.entity.BoardType;
import com.server.capple.domain.member.entity.Member;
import org.springframework.stereotype.Component;

@Component
public class BoardMapper {

    public Board toBoard(Member member, BoardType boardType, String content) {
        return Board.builder()
                .writer(member)
                .boardType(boardType)
                .content(content)
                .commentCount(0)
                .heartCount(0)
                .isReport(Boolean.FALSE)
                .build();
    }

    //redis
    public BoardInfo toBoardInfo(Board board, Integer boardHeartsCount, Boolean isLiked, Boolean isMine) {
        return BoardInfo.builder()
                .boardId(board.getId())
                .writerId(board.getWriter().getId())
                .content(board.getContent())
                .heartCount(boardHeartsCount)
                .commentCount(board.getCommentCount())
                .createAt(board.getCreatedAt())
                .isLiked(isLiked)
                .isMine(isMine)
                .isReported(board.getIsReport())
                .build();
    }

    //rdb
    public BoardInfo toBoardInfo(Board board, Boolean isLiked, Boolean isMine) {
        return BoardInfo.builder()
                .boardId(board.getId())
                .writerId(board.getWriter().getId())
                .content(board.getContent())
                .heartCount(board.getHeartCount())
                .commentCount(board.getCommentCount())
                .createAt(board.getCreatedAt())
                .isLiked(isLiked)
                .isMine(isMine)
                .isReported(board.getIsReport())
                .build();
    }
}

