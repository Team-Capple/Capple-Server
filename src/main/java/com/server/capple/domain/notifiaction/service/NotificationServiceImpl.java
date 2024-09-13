package com.server.capple.domain.notifiaction.service;

import com.server.capple.config.apns.dto.ApnsClientRequest.BoardCommentNotificationBody;
import com.server.capple.config.apns.dto.ApnsClientRequest.BoardNotificationBody;
import com.server.capple.config.apns.service.ApnsService;
import com.server.capple.domain.board.entity.Board;
import com.server.capple.domain.boardComment.entity.BoardComment;
import com.server.capple.domain.boardSubscribeMember.service.BoardSubscribeMemberService;
import com.server.capple.domain.member.entity.Member;
import com.server.capple.domain.notifiaction.dto.NotificationResponse.NotificationInfo;
import com.server.capple.domain.notifiaction.entity.Notification;
import com.server.capple.domain.notifiaction.mapper.NotificationMapper;
import com.server.capple.domain.notifiaction.repository.NotificationRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

import static com.server.capple.domain.notifiaction.entity.NotificationType.*;

@Service
@RequiredArgsConstructor
public class NotificationServiceImpl implements NotificationService {
    private final ApnsService apnsService;
    private final BoardSubscribeMemberService boardSubscribeMemberService;
    private final NotificationRepository notificationRepository;
    private final NotificationMapper notificationMapper;

    @Override
    public void sendBoardHeartNotification(Long actorId, Board board) {
        if (actorId.equals(board.getWriter().getId())) return;
        BoardNotificationBody boardNotificationBody = BoardNotificationBody.builder()
            .type(BOARD_HEART)
            .board(board)
            .build();
        apnsService.sendApnsToMembers(boardNotificationBody, board.getWriter().getId());
        // TODO 알림 데이터베이스 저장
        Notification notification = notificationMapper.toNotification(board.getWriter().getId(), boardNotificationBody);
        notificationRepository.save(notification);
    }

    @Override
    @Transactional
    public void sendBoardCommentNotification(Long actorId, Board board, BoardComment boardComment) {
        List<Member> subscribers = boardSubscribeMemberService.findBoardSubscribeMembers(board.getId());
        List<Long> subscriberIds = subscribers.stream()
            .map(Member::getId)
            .filter(id -> !id.equals(actorId))
//        게시판 구독자에게 알림 전송
            .peek(subscriberId -> {
                if (subscriberId.equals(board.getWriter().getId())) {
                    BoardCommentNotificationBody boardCommentNotificationBody = BoardCommentNotificationBody.builder()
                        .type(BOARD_COMMENT)
                        .board(board)
                        .boardComment(boardComment)
                        .build();
                    apnsService.sendApnsToMembers(boardCommentNotificationBody, subscriberId);
                    notificationRepository.save(notificationMapper.toNotification(subscriberId, boardCommentNotificationBody));
                }
            })
            .filter(id -> !id.equals(board.getWriter().getId()))
            .toList();
        BoardCommentNotificationBody boardCommentNotificationBody = BoardCommentNotificationBody.builder()
            .type(BAORD_COMMENT_DUPLCATE)
            .board(board)
            .boardComment(boardComment)
            .build();
        apnsService.sendApnsToMembers(boardCommentNotificationBody, subscriberIds);
        // TODO 알림 데이터베이스 저장
        List<Notification> notifications = subscriberIds.stream()
            .map(subscriberId -> notificationMapper.toNotification(subscriberId, boardCommentNotificationBody))
            .toList();
        notificationRepository.saveAll(notifications);
    }

    @Override
    public void sendBoardCommentHeartNotification(Long actorId, Board board, BoardComment boardComment) {
        BoardCommentNotificationBody boardCommentNotificationBody = BoardCommentNotificationBody.builder()
            .type(BOARD_COMMENT_HEART)
            .board(board)
            .boardComment(boardComment)
            .build();
        apnsService.sendApnsToMembers(boardCommentNotificationBody, boardComment.getMember().getId());
        // TODO 알림 데이터베이스 저장
        Notification notification = notificationMapper.toNotification(boardComment.getMember().getId(), boardCommentNotificationBody);
        notificationRepository.save(notification);
    }

    @Override
    public Slice<NotificationInfo> getNotifications(Member member, Pageable pageable) {
        return notificationRepository.findByMemberId(member.getId(), pageable, NotificationInfo.class);
    }

    @Override
    @Transactional
    public void deleteNotificationsByCreatedAtBefore(LocalDateTime targetTime) {
        notificationRepository.deleteNotificationsByCreatedAtBefore(targetTime);
    }
}
