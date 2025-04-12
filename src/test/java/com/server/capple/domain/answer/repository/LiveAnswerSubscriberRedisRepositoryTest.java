package com.server.capple.domain.answer.repository;

import com.server.capple.support.ServiceTestConfig;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.test.context.ActiveProfiles;

import java.util.Collection;
import java.util.List;

import static com.server.capple.domain.answer.repository.LiveAnswerSubscriberRedisRepository.LIVE_ANSWER_SUBSCRIBER_KEY;
import static org.assertj.core.api.Assertions.assertThat;


@SpringBootTest
@ActiveProfiles("test")
@DisplayName("live subscriber redis 레포지토리로 ")
class LiveAnswerSubscriberRedisRepositoryTest extends ServiceTestConfig {
    @Autowired
    LiveAnswerSubscriberRedisRepository liveAnswerSubscriberRedisRepository;
    @Autowired
    @Qualifier("longRedisTemplate")
    RedisTemplate<String, Long> redisTemplate;

    @DisplayName("새로운 구독자를 추가할 수 있다.")
    @TestFactory
    Collection<DynamicTest> addMember() {
        return List.of(
            DynamicTest.dynamicTest("1명 추가", () -> {
                // given
                Long memberId = 1L;

                // when
                liveAnswerSubscriberRedisRepository.addMember(memberId);

                // then
                assertThat(redisTemplate.opsForList().range(LIVE_ANSWER_SUBSCRIBER_KEY, 0, -1)).hasSize(1)
                    .containsExactly(memberId);
            }),
            DynamicTest.dynamicTest("2명 추가", () -> {
                // given
                Long memberId = 2L;

                // when
                liveAnswerSubscriberRedisRepository.addMember(memberId);

                // then
                assertThat(redisTemplate.opsForList().range(LIVE_ANSWER_SUBSCRIBER_KEY, 0, -1)).hasSize(2)
                    .containsExactly(1L, memberId);
            })
        );
    }

    @Test
    @DisplayName("removeAll")
    void removeAll() {
        // given
        redisTemplate.opsForList().rightPushAll(LIVE_ANSWER_SUBSCRIBER_KEY, List.of(1L, 2L, 3L, 4L));

        // when
        liveAnswerSubscriberRedisRepository.removeAll();

        // then
        assertThat(redisTemplate.opsForList().range(LIVE_ANSWER_SUBSCRIBER_KEY, 0, -1)).isEmpty();
    }

    @DisplayName("getSubscriber")
    @TestFactory
    Collection<DynamicTest> getSubscriber() {
        return List.of(
            DynamicTest.dynamicTest("1명 추가", () -> {
                // given
                Long memberId = 1L;
                redisTemplate.opsForList().rightPush(LIVE_ANSWER_SUBSCRIBER_KEY, memberId);

                // when
                List<Long> subscriber = liveAnswerSubscriberRedisRepository.getSubscriber();

                // then
                assertThat(subscriber).hasSize(1)
                    .containsExactly(
                        memberId
                    );
            }),
            DynamicTest.dynamicTest("2명 추가", () -> {
                // given
                Long memberId = 2L;
                redisTemplate.opsForList().rightPush(LIVE_ANSWER_SUBSCRIBER_KEY, memberId);

                // when
                List<Long> subscriber = liveAnswerSubscriberRedisRepository.getSubscriber();

                // then
                assertThat(subscriber).hasSize(2)
                    .containsExactly(
                        1L, memberId
                    );
            }),
            DynamicTest.dynamicTest("4명 추가", () -> {
                // given
                redisTemplate.opsForList().rightPushAll(LIVE_ANSWER_SUBSCRIBER_KEY, List.of(3L, 4L));

                // when
                List<Long> subscriber = liveAnswerSubscriberRedisRepository.getSubscriber();

                // then
                assertThat(subscriber).hasSize(4)
                    .containsExactly(
                        1L, 2L, 3L, 4L
                    );
            })
        );
    }
}