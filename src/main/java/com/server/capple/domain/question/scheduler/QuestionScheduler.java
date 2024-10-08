package com.server.capple.domain.question.scheduler;

import com.server.capple.domain.notifiaction.service.NotificationService;
import com.server.capple.domain.question.entity.Question;
import com.server.capple.domain.question.service.AdminQuestionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class QuestionScheduler {
    private final AdminQuestionService adminQuestionService;
    private final NotificationService notificationService;

    //초 분 시 일 월 요일
    @Scheduled(cron = "0 0 7,18 * * *") //매일 오전 7시에, 오후 6시에
    public void setLiveQuestion() {
        Question question = adminQuestionService.setLiveQuestion();
        notificationService.sendLiveQuestionOpenNotification(question);
        log.info("live question이 등록되었습니다.");
    }

    @Scheduled(cron = "0 0 1,14 * * *") //매일 오전 1시에, 오후 14시에
    public void closeLiveQuestion() {
        //question을 닫음
        Question question = adminQuestionService.closeLiveQuestion();
        notificationService.sendLiveQuestionCloseNotification(question);
        log.info("live question이 닫혔습니다.");
    }
}
