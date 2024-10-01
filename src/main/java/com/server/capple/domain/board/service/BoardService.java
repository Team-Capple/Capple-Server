package com.server.capple.domain.board.service;

import com.server.capple.domain.board.dto.BoardResponse.BoardId;
import com.server.capple.domain.board.dto.BoardResponse.BoardInfo;
import com.server.capple.domain.board.dto.BoardResponse.ToggleBoardHeart;
import com.server.capple.domain.board.entity.Board;
import com.server.capple.domain.board.entity.BoardType;
import com.server.capple.domain.member.entity.Member;
import com.server.capple.global.common.SliceResponse;
import org.springframework.data.domain.Pageable;


public interface BoardService {
    BoardId createBoard(Member member, BoardType boardType, String content);

    SliceResponse<BoardInfo> getBoardsByBoardTypeWithRedis(Member member, BoardType boardType, Long lastIndex, Pageable pageable);

    SliceResponse<BoardInfo> getBoardsByBoardType(Member member, BoardType boardType, Long lastIndex, Pageable pageable);

    SliceResponse<BoardInfo> searchBoardsByKeyword(Member member, String keyword, Long lastIndex, Pageable pageable);

    BoardId deleteBoard(Member member, Long boardId);

    ToggleBoardHeart toggleBoardHeart(Member member, Long boardId);

    Board findBoard(Long boardId);
}
