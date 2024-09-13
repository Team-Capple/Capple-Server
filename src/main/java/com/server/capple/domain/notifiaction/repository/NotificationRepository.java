package com.server.capple.domain.notifiaction.repository;

import com.server.capple.domain.notifiaction.entity.Notification;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NotificationRepository extends JpaRepository<Notification, Long> {
}
