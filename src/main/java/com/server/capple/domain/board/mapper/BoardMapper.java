package com.server.capple.domain.board.mapper;

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
            Board board,
            Integer boardHeartsCount,
            Boolean isLiked,
            Boolean isMine,
            Boolean isReported) {
        return BoardResponse.BoardsGetByBoardTypeBoardInfo.builder()
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

    public BoardResponse.BoardDelete toBoardDelete(Board board) {
        return BoardResponse.BoardDelete.builder()
                .boardId(board.getId())
                .build();
    }

    public BoardResponse.BoardsSearchByKeywordBoardInfo toBoardsSearchByKeywordBoardInfo(
            Board board,
            Integer heartCount
    ) {
        return BoardResponse.BoardsSearchByKeywordBoardInfo.builder()
                .boardId(board.getId())
                .writerId(board.getWriter().getId())
                .content(board.getContent())
                .heartCount(heartCount)
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
