package com.server.capple.domain.board.repository;

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
public class BoardHeartRedisRepository implements Serializable {
    public static final String BOARD_HEART_KEY_PREFIX = "boardHeart-";
    public static final String MEMBER_KEY_PREFIX = "member-";

    private final RedisTemplate<String, String> redisTemplate;

    // 게시판 좋아요 토글
    public Boolean toggleBoardHeart(Long memberId, Long boardId) {
        String key = BOARD_HEART_KEY_PREFIX + boardId.toString();
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

    // 게시판 좋아요 수 조회
    public Integer getBoardHeartsCount(Long boardId) {
        String key = BOARD_HEART_KEY_PREFIX + boardId.toString();
        Set<String> members = redisTemplate.opsForSet().members(key);
        return members != null ? members.size() : 0;
    }

    // 좋아요 누른 게시판 조회
    public Set<Long> getMemberHeartsBoard(Long memberId) {
        String member = MEMBER_KEY_PREFIX + memberId.toString();
        Set<String> keys = redisTemplate.keys(BOARD_HEART_KEY_PREFIX + "*"); // 모든 키 조회
        Set<Long> boardIds = new HashSet<>();

        for (String key : keys) {
            if (redisTemplate.opsForSet().isMember(key, member)) {
                String boardId = key.substring(BOARD_HEART_KEY_PREFIX.length());
                boardIds.add(Long.parseLong(boardId));
            }
        }
        return boardIds;
    }
}