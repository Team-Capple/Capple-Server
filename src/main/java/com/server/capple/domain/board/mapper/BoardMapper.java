package com.server.capple.domain.board.mapper;

import com.server.capple.domain.board.dto.BoardRequest;
import com.server.capple.domain.board.dto.BoardResponse;
import com.server.capple.domain.board.entity.Board;
import com.server.capple.domain.board.entity.BoardType;
import com.server.capple.domain.member.entity.Member;
import org.springframework.stereotype.Component;

import java.util.List;

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
                .writer(member)
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

    public BoardResponse.BoardsGetByBoardType toBoardsGetByBoardType(
            List<BoardResponse.BoardsGetByBoardTypeBoardInfo> boards
    ) {
        return BoardResponse.BoardsGetByBoardType.builder()
                .boards(boards)
                .build();
    }

    public BoardResponse.BoardsGetByBoardTypeBoardInfo toBoardsGetByBoardTypeBoardInfo(
            Board board
    ) {
        return BoardResponse.BoardsGetByBoardTypeBoardInfo.builder()
                .writerId(board.getWriter().getId())
                .content(board.getContent())
                .heartCount(board.getHeartCount())
                .commentCount(board.getCommentCount())
                .createAt(board.getCreatedAt())
                .build();
    }

    public BoardResponse.BoardDelete toBoardDelete(Board board) {
        return BoardResponse.BoardDelete.builder()
                .boardId(board.getId())
                .build();
    }

    public BoardResponse.BoardsSearchByKeywordBoardInfo toBoardsSearchByKeywordBoardInfo(
            Board board
    ) {
        return BoardResponse.BoardsSearchByKeywordBoardInfo.builder()
                .writerId(board.getWriter().getId())
                .content(board.getContent())
                .heartCount(board.getHeartCount())
                .commentCount(board.getCommentCount())
                .createAt(board.getCreatedAt())
                .build();
    }

    public BoardResponse.BoardsSearchByKeyword toBoardsSearchByKeyword(
            List<BoardResponse.BoardsSearchByKeywordBoardInfo> boards
    ) {
        return BoardResponse.BoardsSearchByKeyword.builder()
                .boards(boards)
                .build();
    }
}
