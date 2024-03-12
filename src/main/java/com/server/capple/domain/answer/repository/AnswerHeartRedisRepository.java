package com.server.capple.domain.answer.repository;

import jakarta.annotation.Resource;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.Cursor;
import org.springframework.data.redis.core.ScanOptions;
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

    @Resource(name = "redisTemplate")
    private SetOperations<String, String> setOperations;

    public Boolean toggleAnswerHeart(Long memberId, Long answerId) {
        String key = ANSWER_HEART_KEY_PREFIX + answerId.toString();
        String member = MEMBER_KEY_PREFIX + memberId.toString();

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
        return setOperations.members(key).size();
    }

    public Set<Integer> getMemberHeartsAnswer(Long memberId) {
        String member = MEMBER_KEY_PREFIX + memberId.toString();
        ScanOptions option = ScanOptions.scanOptions().match("*").build();

        Cursor<String> cursor = setOperations.scan(member, option);
        Set<Integer> memberHeartAnswerIds = new HashSet<>();

        while(cursor.hasNext()){
            String value = cursor.next();
            Integer answerId = Integer.parseInt(value.replace(ANSWER_HEART_KEY_PREFIX, ""));
            memberHeartAnswerIds.add(answerId);
        }
        return memberHeartAnswerIds;
    }
}
