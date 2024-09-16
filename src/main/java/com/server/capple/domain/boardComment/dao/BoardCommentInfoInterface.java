package com.server.capple.domain.boardComment.dao;

import com.server.capple.domain.boardComment.entity.BoardComment;

public interface BoardCommentInfoInterface {
    BoardComment getBoardComment();
    Boolean getIsLike();
    Boolean getIsMine();
}
