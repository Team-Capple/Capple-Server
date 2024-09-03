package com.server.capple.domain.board.service;

import com.server.capple.domain.board.dto.BoardResponse;
import com.server.capple.domain.board.dto.BoardResponse.*;
import com.server.capple.domain.board.entity.Board;
import com.server.capple.domain.board.entity.BoardType;
import com.server.capple.domain.member.entity.Member;
import org.springframework.data.domain.Pageable;

public interface BoardService {
    BoardCreate createBoard(Member member, BoardType boardType, String content);

    BoardsGetByBoardType getBoardsByBoardType(Member member, BoardType boardType);

    BoardDelete deleteBoard(Member member, Long boardId);

    BoardsSearchByKeyword searchBoardsByKeyword(String keyword);

    BoardToggleHeart toggleBoardHeart(Member member, Long boardId);
    Board findBoard(Long boardId);
}
