package com.server.capple.domain.notifiaction.service;

import com.server.capple.domain.board.entity.Board;
import com.server.capple.domain.boardComment.entity.BoardComment;
import com.server.capple.domain.member.entity.Member;
import com.server.capple.domain.notifiaction.dto.NotificationResponse;
import com.server.capple.domain.question.entity.Question;
import com.server.capple.global.common.SliceResponse;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;

public interface NotificationService {
    void sendBoardHeartNotification(Long actorId, Board board);
    void sendBoardCommentNotification(Long actorId, Board board, BoardComment boardComment);
    void sendBoardCommentHeartNotification(Long actorId, Board board, BoardComment boardComment);
    void sendLiveQuestionOpenNotification(Question question);
    void sendLiveQuestionCloseNotification(Question question);
    SliceResponse<NotificationResponse.NotificationInfo> getNotifications(Member member, Long lastIndex, Pageable pageable);
    void deleteNotificationsByCreatedAtBefore(LocalDateTime targetTime);
}
