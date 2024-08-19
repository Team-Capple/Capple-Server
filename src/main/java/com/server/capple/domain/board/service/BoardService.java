package com.server.capple.domain.board.service;

import com.server.capple.domain.board.dto.BoardResponse;
import com.server.capple.domain.board.entity.BoardType;
import com.server.capple.domain.member.entity.Member;

public interface BoardService {
    BoardResponse.BoardCreate createBoard(Member member, BoardType boardType, String content);
}
