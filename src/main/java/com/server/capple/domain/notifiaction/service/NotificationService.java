package com.server.capple.domain.notifiaction.service;

import com.server.capple.domain.board.entity.Board;
import com.server.capple.domain.boardComment.entity.BoardComment;
import com.server.capple.domain.member.entity.Member;
import com.server.capple.domain.notifiaction.dto.NotificationResponse;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

public interface NotificationService {
    void sendBoardHeartNotification(Long actorId, Board board);
    void sendBoardCommentNotification(Long actorId, Board board, BoardComment boardComment);
    void sendBoardCommentHeartNotification(Long actorId, Board board, BoardComment boardComment);
    Slice<NotificationResponse.NotificationInfo> getNotifications(Member member, Pageable pageable);
}
