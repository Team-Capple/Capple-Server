package com.server.capple.domain.notifiaction.mapper;

import com.server.capple.domain.answer.entity.Answer;
import com.server.capple.domain.answerComment.entity.AnswerComment;
import com.server.capple.domain.board.entity.Board;
import com.server.capple.domain.boardComment.entity.BoardComment;
import com.server.capple.domain.member.entity.Member;
import com.server.capple.domain.notifiaction.dto.NotificationDBResponse.NotificationDBInfo;
import com.server.capple.domain.notifiaction.dto.NotificationResponse.NotificationInfo;
import com.server.capple.domain.notifiaction.entity.Notification;
import com.server.capple.domain.notifiaction.entity.NotificationLog;
import com.server.capple.domain.notifiaction.entity.NotificationType;
import com.server.capple.domain.question.entity.Question;
import com.server.capple.global.common.SliceResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class NotificationMapper {
    public Notification toNotification(Member member, NotificationLog notificationLog, NotificationType type) {
        return Notification.builder()
            .member(member)
            .notificationLog(notificationLog)
            .type(type)
            .build();
    }

    public Notification toNotification(NotificationLog notificationLog, NotificationType type) {
        return Notification.builder()
            .notificationLog(notificationLog)
            .type(type)
            .build();
    }

    public NotificationLog toNotificationLog(Board board) {
        return NotificationLog.builder()
            .body(board.getContent())
            .board(board)
            .build();
    }

    public NotificationLog toNotificationLog(Board board, BoardComment boardComment) {
        return NotificationLog.builder()
            .body(boardComment.getContent())
            .board(board)
            .boardComment(boardComment)
            .build();
    }

    public NotificationLog toNotificationLog(Question question) {
        return NotificationLog.builder()
            .body(question.getContent())
            .question(question)
            .build();
    }

    public NotificationLog toNotificationLog(Question question, Answer answer) {
        return NotificationLog.builder()
            .question(question)
            .answer(answer)
            .build();
    }

    public NotificationLog toNotificationLog(Question question, Answer answer, AnswerComment answerComment) {
        return NotificationLog.builder()
            .question(question)
            .answer(answer)
            .answerComment(answerComment)
            .build();
    }

    public SliceResponse<NotificationInfo> toNotificationInfoSlice(Slice<NotificationDBInfo> notificationDBInfo, Long lastIndex) {
        return SliceResponse.toSliceResponse(notificationDBInfo, notificationDBInfo.stream().map(this::toNotificationInfo).toList(), lastIndex.toString(), null);
    }

    private NotificationInfo toNotificationInfo(NotificationDBInfo notificationDBInfo) {
        return switch (notificationDBInfo.getNotification().getType()) {
            case BOARD_HEART -> toBoardNotificationInfo(notificationDBInfo);
            case BOARD_COMMENT, BOARD_COMMENT_DUPLICATE, BOARD_COMMENT_HEART ->
                toBoardCommentNotificationInfo(notificationDBInfo);
            case TODAY_QUESTION_PUBLISHED, TODAY_QUESTION_CLOSED -> toQuestionNotificationInfo(notificationDBInfo);
            case LIVE_QUESTION_ANSWER_ADDED -> toQuestionLiveAnswerAddedNotificationInfo(notificationDBInfo);
            case NEW_FREE_BOARD -> toNewBoardNotificationInfo(notificationDBInfo);
            case ANSWER_HEART  -> toAnswerNotificationInfo(notificationDBInfo);
            case ANSWER_COMMENT, ANSWER_COMMENT_DUPLICATE, ANSWER_COMMENT_HEART -> toAnswerCommentNotificationInfo(notificationDBInfo);
        };
    }

    private NotificationInfo toBoardNotificationInfo(NotificationDBInfo notificationDBInfo) {
        return NotificationInfo.builder()
            .title(notificationDBInfo.getNotification().getType().getTitle())
            .content(notificationDBInfo.getNotification().getNotificationLog().getBody())
            .boardId(notificationDBInfo.getNotification().getNotificationLog().getBoard().getId().toString())
            .isReportedBoard(notificationDBInfo.getIsReportedBoard())
            .createdAt(notificationDBInfo.getNotification().getCreatedAt())
            .build();
    }

    private NotificationInfo toBoardCommentNotificationInfo(NotificationDBInfo notificationDBInfo) {
        return NotificationInfo.builder()
            .title(notificationDBInfo.getNotification().getType().getTitle())
            .content(notificationDBInfo.getNotification().getNotificationLog().getBody())
            .boardId(notificationDBInfo.getNotification().getNotificationLog().getBoard().getId().toString())
            .isReportedBoard(notificationDBInfo.getIsReportedBoard())
            .boardCommentId(notificationDBInfo.getNotification().getNotificationLog().getBoardComment().getId().toString())
            .createdAt(notificationDBInfo.getNotification().getCreatedAt())
            .build();
    }

    private NotificationInfo toQuestionNotificationInfo(NotificationDBInfo notificationDBInfo) {
        return NotificationInfo.builder()
            .title(notificationDBInfo.getNotification().getType().getTitle())
            .content(notificationDBInfo.getNotification().getNotificationLog().getBody())
            .questionId(notificationDBInfo.getNotification().getNotificationLog().getQuestion().getId().toString())
            .isResponsedQuestion(notificationDBInfo.getIsResponsedQuestion())
            .createdAt(notificationDBInfo.getNotification().getCreatedAt())
            .build();
    }

    private NotificationInfo toQuestionLiveAnswerAddedNotificationInfo(NotificationDBInfo notificationDBInfo) {
        return NotificationInfo.builder()
            .title(notificationDBInfo.getNotification().getType().getTitle())
            .content(notificationDBInfo.getNotification().getType().getBody())
            .questionId(notificationDBInfo.getNotification().getNotificationLog().getQuestion().getId().toString())
            .answerId(notificationDBInfo.getNotification().getNotificationLog().getAnswer().getId().toString())
            .isResponsedQuestion(notificationDBInfo.getIsResponsedQuestion())
            .createdAt(notificationDBInfo.getNotification().getCreatedAt())
            .build();
    }

    private NotificationInfo toNewBoardNotificationInfo(NotificationDBInfo notificationDBInfo) {
        return NotificationInfo.builder()
            .title(notificationDBInfo.getNotification().getType().getTitle())
            .content(notificationDBInfo.getNotification().getType().getBody())
            .boardId(notificationDBInfo.getNotification().getNotificationLog().getBoard().getId().toString())
            .isReportedBoard(notificationDBInfo.getIsReportedBoard())
            .createdAt(notificationDBInfo.getNotification().getCreatedAt())
            .build();
    }

    private NotificationInfo toAnswerNotificationInfo(NotificationDBInfo notificationDBInfo) {
        return NotificationInfo.builder()
            .title(notificationDBInfo.getNotification().getType().getTitle())
            .content(notificationDBInfo.getNotification().getNotificationLog().getAnswer().getContent())
            .questionId(notificationDBInfo.getNotification().getNotificationLog().getQuestion().getId().toString())
            .answerId(notificationDBInfo.getNotification().getNotificationLog().getAnswer().getId().toString())
            .createdAt(notificationDBInfo.getNotification().getCreatedAt())
            .isResponsedQuestion(notificationDBInfo.getIsResponsedQuestion())
            .build();
    }

    private NotificationInfo toAnswerCommentNotificationInfo(NotificationDBInfo notificationDBInfo) {
        return NotificationInfo.builder()
            .title(notificationDBInfo.getNotification().getType().getTitle())
            .content(notificationDBInfo.getNotification().getNotificationLog().getAnswerComment().getContent())
            .questionId(notificationDBInfo.getNotification().getNotificationLog().getQuestion().getId().toString())
            .answerId(notificationDBInfo.getNotification().getNotificationLog().getAnswer().getId().toString())
            .answerCommentId(notificationDBInfo.getNotification().getNotificationLog().getAnswerComment().getId().toString())
            .createdAt(notificationDBInfo.getNotification().getCreatedAt())
            .isResponsedQuestion(notificationDBInfo.getIsResponsedQuestion())
            .build();
    }
}
