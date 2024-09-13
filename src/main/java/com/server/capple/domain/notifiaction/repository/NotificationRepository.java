package com.server.capple.domain.notifiaction.repository;

import com.server.capple.domain.notifiaction.entity.Notification;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;

public interface NotificationRepository extends JpaRepository<Notification, Long> {
    <T> Slice<T> findByMemberId(Long memberId, Pageable pageable, Class<T> type);
    void deleteNotificationsByCreatedAtBefore(LocalDateTime targetTime);
}
