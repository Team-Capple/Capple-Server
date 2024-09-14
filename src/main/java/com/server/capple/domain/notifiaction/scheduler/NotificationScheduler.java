package com.server.capple.domain.notifiaction.scheduler;

import com.server.capple.domain.notifiaction.service.NotificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Slf4j
@Component
@RequiredArgsConstructor
public class NotificationScheduler {
    private final NotificationService notificationService;
    private final long NOTIFICATION_CACHE_WEEK = 1L;

    @Scheduled(cron = "0 0 * * * *") //매 0분에
    public void deleteNotifications() {
        LocalDateTime targetTime = LocalDateTime.now().minusWeeks(NOTIFICATION_CACHE_WEEK);
        notificationService.deleteNotificationsByCreatedAtBefore(targetTime);
        log.info("오래된 알림을 제거했습니다. 제거 기한 : " + targetTime);
    }
}
