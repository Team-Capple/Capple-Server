package com.server.capple.domain.tag.scheduler;

import com.server.capple.domain.tag.repository.TagRedisRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class TagScheduler {
    private final TagRedisRepository tagRedisRepository;

    @Scheduled(cron = "0 0 0 * * *") //매일 밤 12시에 실행
    public void decreaseTagCount() {
        tagRedisRepository.decreaseTagCount();
        System.out.println("success");
    }
}
