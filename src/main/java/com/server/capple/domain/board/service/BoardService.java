package com.server.capple.domain.board.service;

import com.server.capple.domain.board.dto.BoardResponse.BoardId;
import com.server.capple.domain.board.dto.BoardResponse.BoardsGetBoardInfos;
import com.server.capple.domain.board.dto.BoardResponse.ToggleBoardHeart;
import com.server.capple.domain.board.entity.Board;
import com.server.capple.domain.board.entity.BoardType;
import com.server.capple.domain.member.entity.Member;

public interface BoardService {
    BoardId createBoard(Member member, BoardType boardType, String content);

    BoardsGetBoardInfos getBoardsByBoardTypeWithRedis(Member member, BoardType boardType);

    BoardsGetBoardInfos getBoardsByBoardType(Member member, BoardType boardType);

    BoardId deleteBoard(Member member, Long boardId);

    BoardsGetBoardInfos searchBoardsByKeyword(Member member, String keyword);

    ToggleBoardHeart toggleBoardHeart(Member member, Long boardId);

    Board findBoard(Long boardId);
}
