package com.server.capple.domain.board.dao;

import com.server.capple.domain.board.entity.Board;
import com.server.capple.domain.member.entity.AcademyGeneration;

import java.util.Optional;

public interface BoardInfoInterface {
    Board getBoard();
    Boolean getIsLike();
    Boolean getIsMine();
    String getWriterNickname();
    Optional<AcademyGeneration> getWriterAcademyGeneration();
}
