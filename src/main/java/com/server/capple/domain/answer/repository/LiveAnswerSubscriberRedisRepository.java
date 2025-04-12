package com.server.capple.domain.answer.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class LiveAnswerSubscriberRedisRepository {
    public static final String LIVE_ANSWER_SUBSCRIBER_KEY = "live-answer-subscriber";

    @Qualifier("longRedisTemplate")
    private final RedisTemplate<String, Long> redisTemplate;

    public void addMember(Long memberId) {
        redisTemplate.opsForList().rightPush(LIVE_ANSWER_SUBSCRIBER_KEY, memberId);
    }

    public void removeAll() {
        redisTemplate.delete(LIVE_ANSWER_SUBSCRIBER_KEY);
    }

    public List<Long> getSubscriber() {
        return redisTemplate.opsForList().range(LIVE_ANSWER_SUBSCRIBER_KEY, 0, -1);
    }
}
