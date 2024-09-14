package com.server.capple.domain.board.service;

import com.server.capple.domain.board.dto.BoardResponse.*;
import com.server.capple.domain.board.entity.Board;
import com.server.capple.domain.board.entity.BoardType;
import com.server.capple.domain.member.entity.Member;

public interface BoardService {
    BoardCreate createBoard(Member member, BoardType boardType, String content);

    BoardsGetByBoardType getBoardsByBoardTypeWithRedis(Member member, BoardType boardType);

    BoardsGetByBoardType getBoardsByBoardType(Member member, BoardType boardType);

    BoardDelete deleteBoard(Member member, Long boardId);

    BoardsSearchByKeyword searchBoardsByKeyword(String keyword);

    ToggleBoardHeart toggleBoardHeart(Member member, Long boardId);

    Board findBoard(Long boardId);
}
