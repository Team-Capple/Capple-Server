package com.server.capple.domain.notifiaction.service;

import com.server.capple.config.apns.dto.ApnsClientRequest;
import com.server.capple.config.apns.service.ApnsService;
import com.server.capple.domain.board.entity.Board;
import com.server.capple.domain.boardComment.entity.BoardComment;
import com.server.capple.domain.boardSubscribeMember.service.BoardSubscribeMemberService;
import com.server.capple.domain.member.entity.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.server.capple.domain.notifiaction.entity.NotificationType.*;

@Service
@RequiredArgsConstructor
public class NotificationServiceImpl implements NotificationService {
    private final ApnsService apnsService;
    private final BoardSubscribeMemberService boardSubscribeMemberService;

    @Override
    public void sendBoardHeartNotification(Long actorId, Board board) {
        if (actorId.equals(board.getWriter().getId())) return;
        apnsService.sendApnsToMembers(ApnsClientRequest.SimplePushBody.builder()
            .title(BOARD_HEART.getTitle())
            .body(board.getContent())
            .threadId("board-" + board.getId())
            .sound("default")
            .build(), board.getWriter().getId());
        // TODO 알림 데이터베이스 저장
    }

    @Override
    public void sendBoardCommentNotification(Long actorId, Board board, BoardComment boardComment) {
        List<Member> subscribers = boardSubscribeMemberService.findBoardSubscribeMembers(board.getId());
        List<Long> subscriberIds = subscribers.stream()
            .map(Member::getId)
            .filter(id -> !id.equals(actorId))
//        게시판 구독자에게 알림 전송
            .peek(subscriberId -> {
                if (subscriberId.equals(board.getWriter().getId())) {
                    apnsService.sendApnsToMembers(ApnsClientRequest.SimplePushBody.builder()
                        .title(BOARD_COMMENT.getTitle())
                        .subTitle(boardComment.getContent())
                        .body(board.getContent())
                        .threadId("board-" + board.getId())
                        .sound("default")
                        .build(), subscriberId);
                }
            })
            .filter(id -> !id.equals(board.getWriter().getId()))
            .toList();
        ApnsClientRequest.SimplePushBody simplePushBody = ApnsClientRequest.SimplePushBody.builder()
            .title(BAORD_COMMENT_DUPLCATE.getTitle())
            .subTitle(boardComment.getContent())
            .body(board.getContent())
            .threadId("board-" + board.getId())
            .sound("default")
            .build();
        apnsService.sendApnsToMembers(simplePushBody, subscriberIds);
        // TODO 알림 데이터베이스 저장
    }

    @Override
    public void sendBoardCommentHeartNotification(Long actorId, Board board, BoardComment boardComment) {
        apnsService.sendApnsToMembers(ApnsClientRequest.SimplePushBody.builder()
            .title(BOARD_COMMENT_HEART.getTitle())
            .subTitle(boardComment.getContent())
            .body(board.getContent())
            .threadId("board-" + board.getId())
            .sound("default")
            .build(), boardComment.getMember().getId());
        // TODO 알림 데이터베이스 저장
    }
}
