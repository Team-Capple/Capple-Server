package com.server.capple.domain.notifiaction.dto;

import com.server.capple.domain.answer.entity.Answer;
import com.server.capple.domain.answerComment.entity.AnswerComment;
import com.server.capple.domain.board.entity.Board;
import com.server.capple.domain.notifiaction.entity.Notification;
import com.server.capple.domain.notifiaction.entity.NotificationLog;
import com.server.capple.domain.question.entity.Question;

public class NotificationDBResponse {
    public interface NotificationDBInfo {
        Notification getNotification();
        NotificationLog getNotificationLog();
        Question getNotificationLogQuestion();
        Answer getNotificationLogAnswer();
        Board getNotificationLogBoard();
        AnswerComment getNotificationLogAnswerComment();
        Boolean getIsResponsedQuestion();
        Boolean getIsReportedBoard();
    }
}
