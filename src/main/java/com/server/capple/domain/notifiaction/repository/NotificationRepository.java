package com.server.capple.domain.notifiaction.repository;

import com.server.capple.domain.member.entity.Member;
import com.server.capple.domain.notifiaction.entity.Notification;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;

public interface NotificationRepository extends JpaRepository<Notification, Long> {
    @EntityGraph(attributePaths = {"notificationLog"})
    @Query("select " +
        "n " +
        "from Notification n " +
        "where " +
        "(n.id < :lastIndex OR :lastIndex IS NULL) AND " +
        "(" +
        "n.member = :member " +
        "OR " +
        "((n.type = com.server.capple.domain.notifiaction.entity.NotificationType.TODAY_QUESTION_PUBLISHED " +
        "OR " +
        "n.type = com.server.capple.domain.notifiaction.entity.NotificationType.TODAY_QUESTION_CLOSED) " +
        "AND n.createdAt > (select m.createdAt from Member m where m = :member))" +
        ")"
    )
    Slice<Notification> findByMemberId(Member member, Long lastIndex, Pageable pageable);
    void deleteNotificationsByCreatedAtBefore(LocalDateTime targetTime);
}
