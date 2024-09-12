package com.server.capple.domain.question.repository;

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
public class QuestionHeartRedisRepository implements Serializable {
    public static final String QUESTION_HEART_KEY_PREFIX = "questionHeart-";
    public static final String MEMBER_KEY_PREFIX = "member-";

    private final RedisTemplate<String, String> redisTemplate;

    // 질문 좋아요 토글
    public Boolean toggleBoardHeart(Long memberId, Long boardId) {
        String key = QUESTION_HEART_KEY_PREFIX + boardId.toString();
        String member = MEMBER_KEY_PREFIX + memberId.toString();
        SetOperations<String, String> setOperations = redisTemplate.opsForSet();

        //해당 key에 member가 존재하지 않으면 추가, 존재하면 삭제
        if (FALSE.equals(setOperations.isMember(key, member))) {
            setOperations.add(key, member);
            return TRUE;
        } else {
            setOperations.remove(key, member);
            // 좋아요 취소 시 생성 시간도 삭제할 수 있음
            return FALSE;
        }
    }


    public String getQuestionHeartCreateAt(Long questionId, Long memberId) {
        String createAtKey = QUESTION_HEART_KEY_PREFIX + questionId.toString() + ":" + MEMBER_KEY_PREFIX + memberId.toString() + ":createAt";
        return redisTemplate.opsForValue().get(createAtKey);
    }

    // 질문 좋아요 수 조회
    public Integer getQuestionHeartsCount(Long questionId) {
        String key = QUESTION_HEART_KEY_PREFIX + questionId.toString();
        Set<String> members = redisTemplate.opsForSet().members(key);
        return members != null ? members.size() : 0;
    }

    // 좋아요 누른 질문 조회
    public Set<Long> getMemberHeartsQuestion(Long memberId) {
        String member = MEMBER_KEY_PREFIX + memberId.toString();
        Set<String> keys = redisTemplate.keys(QUESTION_HEART_KEY_PREFIX + "*"); // 모든 키 조회
        Set<Long> questionIds = new HashSet<>();

        for (String key : keys) {
            if (redisTemplate.opsForSet().isMember(key, member)) {
                String questionId = key.substring(QUESTION_HEART_KEY_PREFIX.length());
                questionIds.add(Long.parseLong(questionId));
            }
        }
        return questionIds;
    }

    public boolean isMemberLikedQuestion(Long memberId, Long questionId) {
        String key = QUESTION_HEART_KEY_PREFIX + questionId;
        String memberKey = MEMBER_KEY_PREFIX + memberId;
        return redisTemplate.opsForSet().isMember(key, memberKey);
    }
}