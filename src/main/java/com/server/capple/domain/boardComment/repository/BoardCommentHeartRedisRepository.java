package com.server.capple.domain.boardComment.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SetOperations;
import org.springframework.stereotype.Repository;

import java.io.Serializable;

import static java.lang.Boolean.FALSE;
import static java.lang.Boolean.TRUE;

@Repository
@RequiredArgsConstructor
public class BoardCommentHeartRedisRepository implements Serializable {
    private static final String BOARD_COMMENT_HEART_KEY_PREFIX = "boardCommentHeart-";
    private static final String MEMBER_PREFIX = "member-";

    private final RedisTemplate<String,String> redisTemplate;

    public Boolean toggleBoardCommentHeart(Long commentId, Long memberId) {
        String key = BOARD_COMMENT_HEART_KEY_PREFIX + commentId.toString();
        String member = MEMBER_PREFIX + memberId.toString();

        SetOperations<String, String> setOperations = redisTemplate.opsForSet();

        Boolean isLiked = setOperations.isMember(key, member);

        if(FALSE.equals(isLiked)) {
            setOperations.add(key, member);
            return TRUE;
        }
        else {
            setOperations.remove(key, member);
            return FALSE;
        }
    }

    public Boolean isMemberLiked(Long commentId, Long memberId) {
        String key = BOARD_COMMENT_HEART_KEY_PREFIX + commentId.toString();
        String member = MEMBER_PREFIX + memberId.toString();

        SetOperations<String, String> setOperations = redisTemplate.opsForSet();

        return setOperations.isMember(key, member);
    }

    public Integer getBoardCommentsCount(Long commentId) {
        String key = BOARD_COMMENT_HEART_KEY_PREFIX + commentId.toString();
        Long size = redisTemplate.opsForSet().size(key);
        return (size != null) ? size.intValue() : 0;
    }

}
