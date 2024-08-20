package com.server.capple.domain.answerComment.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SetOperations;
import org.springframework.stereotype.Repository;

import java.io.Serializable;

import static java.lang.Boolean.FALSE;
import static java.lang.Boolean.TRUE;

@Repository
@RequiredArgsConstructor
public class AnswerCommentHeartRedisRepository implements Serializable {
    public static final String ANSWER_COMMENT_HEART_KEY_PREFIX = "answerCommentHeart-";
    public static final String MEMBER_KEY_PREFIX = "member-";

    private final RedisTemplate<String, String> redisTemplate;

    public Boolean toggleAnswerCommentHeart(Long commentId, Long memberId) {
        String key = ANSWER_COMMENT_HEART_KEY_PREFIX + commentId.toString();
        String member = MEMBER_KEY_PREFIX + memberId.toString();

        SetOperations<String, String> setOperations = redisTemplate.opsForSet();

        // 유저가 좋아요를 눌렀는지 확인
        Boolean isLiked = setOperations.isMember(key, member);

        // 좋아요 취소
        if (FALSE.equals(isLiked)) {
            setOperations.add(key, member);
            return TRUE;
        } else {
            setOperations.remove(key, member);
            return FALSE;
        }
    }

    public Long getAnswerCommentHeartsCount(Long commentId) {
        String key = ANSWER_COMMENT_HEART_KEY_PREFIX + commentId.toString();
        return redisTemplate.opsForSet().size(key);
    }

}
