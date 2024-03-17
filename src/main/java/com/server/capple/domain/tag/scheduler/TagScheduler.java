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

    @Scheduled(cron = "0 0 4 * * *") //매일 새벽 4시에 실행
    public void decreaseTagCount() {
        tagRedisRepository.decreaseTagCount();
        log.info("전 날 사용된 tag의 count를 50% 감소시켰습니다.");

    }
}
