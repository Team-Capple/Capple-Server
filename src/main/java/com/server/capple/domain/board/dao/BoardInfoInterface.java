package com.server.capple.domain.board.dao;

import com.server.capple.domain.board.entity.Board;

public interface BoardInfoInterface {
    Board getBoard();
    Boolean getIsLike();
    Boolean getIsMine();
}
