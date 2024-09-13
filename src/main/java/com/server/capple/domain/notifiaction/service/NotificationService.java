package com.server.capple.domain.notifiaction.service;

import com.server.capple.domain.board.entity.Board;
import com.server.capple.domain.boardComment.entity.BoardComment;

public interface NotificationService {
    void sendBoardHeartNotification(Long actorId, Board board);
    void sendBoardCommentNotification(Long actorId, Board board, BoardComment boardComment);
    void sendBoardCommentHeartNotification(Long actorId, Board board, BoardComment boardComment);
}
