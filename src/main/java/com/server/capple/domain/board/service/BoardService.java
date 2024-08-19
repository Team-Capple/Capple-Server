package com.server.capple.domain.board.service;

import com.server.capple.domain.board.dto.BoardResponse;
import com.server.capple.domain.board.entity.BoardType;
import com.server.capple.domain.member.entity.Member;
import org.springframework.data.domain.Pageable;

public interface BoardService {
    BoardResponse.BoardCreate createBoard(Member member, BoardType boardType, String content);

    BoardResponse.BoardsGetByBoardType getBoardsByBoardType(BoardType boardType);

    BoardResponse.BoardDelete deleteBoard(Member member, Long boardId);

    BoardResponse.BoardsSearchByKeyword searchBoardsByKeyword(String keyword);
}
