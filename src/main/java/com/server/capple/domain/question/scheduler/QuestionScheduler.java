package com.server.capple.domain.question.scheduler;

import com.server.capple.domain.question.service.AdminQuestionService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class QuestionScheduler {
    private final AdminQuestionService adminQuestionService;
    @Scheduled(cron = "0 7,18 0 * * *") //매일 오전 7시에, 오후 6시에
    public void setLiveQuestion() {
        adminQuestionService.setLiveQuestion();
    }

    @Scheduled(cron = "0 1,14 0 * * *") //매일 오전 1시에, 오후 14시에
    public void closeLiveQuestion() {
        adminQuestionService.closeLiveQuestion();
    }
}
