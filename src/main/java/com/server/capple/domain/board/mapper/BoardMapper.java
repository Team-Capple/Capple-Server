package com.server.capple.domain.board.mapper;

import com.server.capple.domain.board.dto.BoardResponse;
import com.server.capple.domain.board.dto.BoardResponse.BoardsGetBoardInfo;
import com.server.capple.domain.board.dto.BoardResponse.BoardsGetBoardInfos;
import com.server.capple.domain.board.entity.Board;
import com.server.capple.domain.board.entity.BoardType;
import com.server.capple.domain.member.entity.Member;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class BoardMapper {

    public Board toBoard(Member member, BoardType boardType, String content) {
        return Board.builder()
                .writer(member)
                .boardType(boardType)
                .content(content)
                .commentCount(0)
                .heartCount(0)
                .build();
    }

    public BoardResponse.BoardCreate toBoardCreate(Board board) {
        return BoardResponse.BoardCreate.builder()
                .boardId(board.getId())
                .build();
    }

    //redis
    public BoardsGetBoardInfo toBoardsGetBoardInfo(Board board, Integer boardHeartsCount, Boolean isLiked,
            Boolean isMine, Boolean isReported) {
        return BoardsGetBoardInfo.builder()
                .boardId(board.getId())
                .writerId(board.getWriter().getId())
                .content(board.getContent())
                .heartCount(boardHeartsCount)
                .commentCount(board.getCommentCount())
                .createAt(board.getCreatedAt())
                .isLiked(isLiked)
                .isMine(isMine)
                // TODO: BoardReport 관련 테이블 구현 후 수정 요망
                .isReported(isReported)
                .build();
    }

    //rdb
    public BoardsGetBoardInfo toBoardsGetBoardInfo(Board board, Boolean isLiked, Boolean isMine, Boolean isReported) {
        return BoardsGetBoardInfo.builder()
                .boardId(board.getId())
                .writerId(board.getWriter().getId())
                .content(board.getContent())
                .heartCount(board.getHeartCount())
                .commentCount(board.getCommentCount())
                .createAt(board.getCreatedAt())
                .isLiked(isLiked)
                .isMine(isMine)
                // TODO: BoardReport 관련 테이블 구현 후 수정 요망
                .isReported(isReported)
                .build();
    }

    public BoardResponse.BoardDelete toBoardDelete(Board board) {
        return BoardResponse.BoardDelete.builder()
                .boardId(board.getId())
                .build();
    }
}
