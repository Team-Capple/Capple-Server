package com.server.capple.domain.notifiaction.repository;

import com.server.capple.domain.notifiaction.entity.Notification;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;

public interface NotificationRepository extends JpaRepository<Notification, Long> {
    @Query("select " +
        "n " +
        "from Notification n " +
        "where " +
        "n.createdAt <= :thresholdDate AND " +
        "(" +
        "n.memberId = :memberId " +
        "OR " +
        "n.type = com.server.capple.domain.notifiaction.entity.NotificationType.TODAY_QUESTION_PUBLISHED " +
        "OR " +
        "n.type = com.server.capple.domain.notifiaction.entity.NotificationType.TODAY_QUESTION_CLOSED" +
        ")"
    )
    Slice<Notification> findByMemberId(Long memberId, LocalDateTime thresholdDate, Pageable pageable);
    void deleteNotificationsByCreatedAtBefore(LocalDateTime targetTime);
}
