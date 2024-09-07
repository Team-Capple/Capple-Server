package com.server.capple.domain.board.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SetOperations;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Repository;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Random;
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
    public Boolean toggleBoardHeart(Long boardId, Long memberId) {
        String key = BOARD_HEART_KEY_PREFIX + boardId.toString();
        String member = MEMBER_KEY_PREFIX + memberId.toString();
        String createAtKey = key + ":" + member + ":createAt"; // member ID를 포함한 createAtKey
        SetOperations<String, String> setOperations = redisTemplate.opsForSet();
        ValueOperations<String, String> valueOperations = redisTemplate.opsForValue();

        //해당 key에 member가 존재하지 않으면 추가, 존재하면 삭제
        if (FALSE.equals(setOperations.isMember(key, member))) {
            setOperations.add(key, member);
            // 현재 시간을 createAtKey로 저장
            valueOperations.set(createAtKey, LocalDateTime.now().toString());
            return TRUE;
        } else {
            setOperations.remove(key, member);
            // 좋아요 취소 시 생성 시간도 삭제할 수 있음
            redisTemplate.delete(createAtKey);
            return FALSE;
        }
    }

    //
    public String getBoardHeartCreateAt(Long boardId, Long memberId) {
        String createAtKey = BOARD_HEART_KEY_PREFIX + boardId.toString() + ":" + MEMBER_KEY_PREFIX + memberId.toString() + ":createAt";
        return redisTemplate.opsForValue().get(createAtKey);
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


    //더미 데이터 생성용
    public void generateDummyBoardLikes(int boardCount) {
        SetOperations<String, String> setOperations = redisTemplate.opsForSet();
        ValueOperations<String, String> valueOperations = redisTemplate.opsForValue();

        Random random = new Random();
        for (int boardId = 0; boardId < boardCount; boardId++) {
            for (int memberId = 1; memberId <= 10; memberId++) {
                if(random.nextBoolean()) {
                    String key = BOARD_HEART_KEY_PREFIX + boardId;
                    String member = MEMBER_KEY_PREFIX + memberId;
                    setOperations.add(key, member);
                }
            }
        }
    }
}