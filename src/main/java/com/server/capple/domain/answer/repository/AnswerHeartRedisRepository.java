package com.server.capple.domain.answer.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SetOperations;
import org.springframework.stereotype.Repository;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import static java.lang.Boolean.FALSE;
import static java.lang.Boolean.TRUE;

@Repository
@RequiredArgsConstructor
public class AnswerHeartRedisRepository implements Serializable {
    public static final String ANSWER_HEART_KEY_PREFIX = "answerHeart-";
    public static final String MEMBER_KEY_PREFIX = "member-";

    private final RedisTemplate<String, String> redisTemplate;

    public Boolean toggleAnswerHeart(Long memberId, Long answerId) {
        String key = ANSWER_HEART_KEY_PREFIX + answerId.toString();
        String member = MEMBER_KEY_PREFIX + memberId.toString();
        SetOperations<String, String> setOperations = redisTemplate.opsForSet();

        //해당 key에 member가 존재하지 않으면 추가, 존재하면 삭제
        if (FALSE.equals(setOperations.isMember(key, member))) {
            setOperations.add(key, member);
            return TRUE;
        } else {
            setOperations.remove(key, member);
            return FALSE;
        }
    }

    public int getAnswerHeartsCount(Long answerId) {
        String key = ANSWER_HEART_KEY_PREFIX + answerId.toString();
        Set<String> members = redisTemplate.opsForSet().members(key);
        return members != null ? members.size() : 0;
    }

    public Set<Long> getMemberHeartsAnswer(Long memberId) {
        String member = MEMBER_KEY_PREFIX + memberId.toString();
        Set<String> keys = redisTemplate.keys(ANSWER_HEART_KEY_PREFIX + "*"); // 모든 키 조회
        Set<Long> answerIds = new HashSet<>();

        for (String key : keys) {
            if (redisTemplate.opsForSet().isMember(key, member)) {
                String answerId = key.substring(ANSWER_HEART_KEY_PREFIX.length());
                answerIds.add(Long.parseLong(answerId));
            }
        }
        return answerIds;
    }

    public boolean isMemberLikedAnswer(Long memberId, Long answerId) {
        String key = ANSWER_HEART_KEY_PREFIX + answerId;
        String memberKey = MEMBER_KEY_PREFIX + memberId;
        return redisTemplate.opsForSet().isMember(key, memberKey);
    }
}
