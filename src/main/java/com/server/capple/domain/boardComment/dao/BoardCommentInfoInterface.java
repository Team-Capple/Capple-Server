package com.server.capple.domain.boardComment.dao;

import com.server.capple.domain.boardComment.entity.BoardComment;
import com.server.capple.domain.member.entity.AcademyGeneration;

import java.util.Optional;

public interface BoardCommentInfoInterface {
    BoardComment getBoardComment();
    Boolean getIsLike();
    Boolean getIsMine();
    Optional<AcademyGeneration> getWriterAcademyGeneration();
}
