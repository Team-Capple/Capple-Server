package com.server.capple.domain.notifiaction.repository;

import com.server.capple.domain.member.entity.Member;
import com.server.capple.domain.notifiaction.dto.NotificationDBResponse.NotificationDBInfo;
import com.server.capple.domain.notifiaction.entity.Notification;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;

public interface NotificationRepository extends JpaRepository<Notification, Long> {
    @EntityGraph(attributePaths = {"notificationLog", "notificationLog.question", "notificationLog.answer"})
    @Query("SELECT " +
        "n notification" +
        ", CASE WHEN n.notificationLog.question IS NULL THEN NULL " +
        "   ELSE (a.member IS NOT NULL) " +
        "   END AS isResponsedQuestion " +
        ", CASE WHEN n.notificationLog.board IS NULL THEN NULL " +
        "   ELSE (br.board IS NOT NULL) " +
        "   END AS isReportedBoard " +
        "FROM Notification n " +
        "LEFT JOIN Answer a " +
        "   ON (a.question = n.notificationLog.question and a.member = :member) " +
        "LEFT JOIN BoardReport br " +
        "   on (br.board = n.notificationLog.board) " +
        "WHERE " +
        "(n.id < :lastIndex OR :lastIndex IS NULL) AND " +
        "(" +
        "(n.type <> com.server.capple.domain.notifiaction.entity.NotificationType.NEW_FREE_BOARD AND n.member = :member) " +
        "OR " +
        "((n.type = com.server.capple.domain.notifiaction.entity.NotificationType.TODAY_QUESTION_PUBLISHED " +
        "OR " +
        "n.type = com.server.capple.domain.notifiaction.entity.NotificationType.TODAY_QUESTION_CLOSED " +
        "OR " +
        "(n.type = com.server.capple.domain.notifiaction.entity.NotificationType.NEW_FREE_BOARD AND n.member <> :member) " +
        ") " +
        "AND n.createdAt > (select m.createdAt from Member m where m = :member))" +
        ")"
    )
    Slice<NotificationDBInfo> findByMemberId(Member member, Long lastIndex, Pageable pageable);
    void deleteNotificationsByCreatedAtBefore(LocalDateTime targetTime);
}
