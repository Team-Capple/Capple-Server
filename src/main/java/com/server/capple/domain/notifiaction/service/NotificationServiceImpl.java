package com.server.capple.domain.notifiaction.service;

import com.server.capple.config.apns.dto.ApnsClientRequest.BoardCommentNotificationBody;
import com.server.capple.config.apns.dto.ApnsClientRequest.BoardNotificationBody;
import com.server.capple.config.apns.dto.ApnsClientRequest.QuestionNotificationBody;
import com.server.capple.config.apns.service.ApnsService;
import com.server.capple.domain.board.entity.Board;
import com.server.capple.domain.boardComment.entity.BoardComment;
import com.server.capple.domain.boardSubscribeMember.service.BoardSubscribeMemberService;
import com.server.capple.domain.member.entity.Member;
import com.server.capple.domain.notifiaction.dto.NotificationDBResponse.NotificationDBInfo;
import com.server.capple.domain.notifiaction.dto.NotificationResponse.NotificationInfo;
import com.server.capple.domain.notifiaction.entity.Notification;
import com.server.capple.domain.notifiaction.entity.NotificationLog;
import com.server.capple.domain.notifiaction.mapper.NotificationMapper;
import com.server.capple.domain.notifiaction.repository.NotificationRepository;
import com.server.capple.domain.question.entity.Question;
import com.server.capple.global.common.SliceResponse;
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
        // 알림 데이터베이스 저장
        Notification notification = notificationMapper.toNotification(
            board.getWriter(),
            notificationMapper.toNotificationLog(board),
            BOARD_HEART);
        notificationRepository.save(notification);
    }

    @Override
    @Transactional
    public void sendBoardCommentNotification(Long actorId, Board board, BoardComment boardComment) {
        List<Member> subscribers = boardSubscribeMemberService.findBoardSubscribeMembers(board.getId());
        NotificationLog notificationLog = notificationMapper.toNotificationLog(board, boardComment);
        List<Member> subscribersExceptWriter = subscribers.stream()
            .filter(member -> !member.getId().equals(actorId))
//        게시판 구독자에게 알림 전송
            .peek(subscriber -> {
                if (subscriber.getId().equals(board.getWriter().getId())) {
                    BoardCommentNotificationBody boardCommentNotificationBody = BoardCommentNotificationBody.builder()
                        .type(BOARD_COMMENT)
                        .board(board)
                        .boardComment(boardComment)
                        .build();
                    apnsService.sendApnsToMembers(boardCommentNotificationBody, subscriber.getId());
                    notificationRepository.save(notificationMapper.toNotification(
                        subscriber,
                        notificationLog,
                        BOARD_COMMENT));
                }
            })
            .filter(member -> !member.getId().equals(board.getWriter().getId()))
            .toList();
        BoardCommentNotificationBody boardCommentNotificationBody = BoardCommentNotificationBody.builder()
            .type(BOARD_COMMENT_DUPLICATE)
            .board(board)
            .boardComment(boardComment)
            .build();
        apnsService.sendApnsToMembers(boardCommentNotificationBody, subscribersExceptWriter.stream().map(Member::getId).toList());
        // 알림 데이터베이스 저장
        List<Notification> notifications = subscribersExceptWriter.stream()
            .map(subscriber -> notificationMapper.toNotification(
                subscriber,
                notificationLog,
                BOARD_COMMENT_DUPLICATE))
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
        apnsService.sendApnsToMembers(boardCommentNotificationBody, boardComment.getWriter().getId());
        // 알림 데이터베이스 저장
        Notification notification = notificationMapper.toNotification(
            boardComment.getWriter(),
            notificationMapper.toNotificationLog(board, boardComment),
            BOARD_COMMENT_HEART);
        notificationRepository.save(notification);
    }

    @Override
    public void sendLiveQuestionOpenNotification(Question question) {
        QuestionNotificationBody questionNotificationBody = QuestionNotificationBody.builder()
            .type(TODAY_QUESTION_PUBLISHED)
            .question(question)
            .build();
        apnsService.sendApnsToAllMembers(questionNotificationBody);
        Notification notification = notificationMapper.toNotification(
            notificationMapper.toNotificationLog(question),
            TODAY_QUESTION_PUBLISHED);
        notificationRepository.save(notification);
    }

    @Override
    public void sendLiveQuestionCloseNotification(Question question) {
        QuestionNotificationBody questionNotificationBody = QuestionNotificationBody.builder()
            .type(TODAY_QUESTION_CLOSED)
            .question(question)
            .build();
        apnsService.sendApnsToAllMembers(questionNotificationBody);
        Notification notification = notificationMapper.toNotification(
            notificationMapper.toNotificationLog(question),
            TODAY_QUESTION_CLOSED);
        notificationRepository.save(notification);
    }

    @Override
    public SliceResponse<NotificationInfo> getNotifications(Member member, Long lastIndex, Pageable pageable) {
        Slice<NotificationDBInfo> notifications = notificationRepository.findByMemberId(member, lastIndex, pageable);
        lastIndex = getLastIndexFromNotification(notifications);
        return notificationMapper.toNotificationInfoSlice(notifications, lastIndex);
    }

    @Override
    @Transactional
    public void deleteNotificationsByCreatedAtBefore(LocalDateTime targetTime) {
        notificationRepository.deleteNotificationsByCreatedAtBefore(targetTime);
    }

    private Long getLastIndexFromNotification(Slice<NotificationDBInfo> notifications) {
        if(notifications.hasContent())
            return notifications.stream().map(NotificationDBInfo::getNotification).map(Notification::getId).min(Long::compareTo).get();
        return -1L;
    }
}
