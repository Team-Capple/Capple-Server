package com.server.capple.domain.tag.repository;


import jakarta.annotation.Resource;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.stereotype.Repository;

import java.io.Serializable;
import java.util.List;
import java.util.Set;

@Repository
@RequiredArgsConstructor
public class TagRedisRepository implements Serializable {
    public static final String TAGS_KEY = "tags";
    public static final String QUESTION_TAGS_KEY_PREFIX = "questionTag-";

    @Resource(name = "redisTemplate")
    private ZSetOperations<String, String> zSetOperations;

    //지금 뜨는 키워드 조회를 위한 tag 저장, tagCount update
    public void saveTags(List<String> tags) {
        tags.forEach(tag -> increaseTagCount(TAGS_KEY, tag));
    }

    //질문에 따른 tag 저장, tagCount update
    public void saveQuestionTags(Long questionId, List<String> tags) {
        String key = QUESTION_TAGS_KEY_PREFIX + questionId.toString();
        tags.forEach(tag -> increaseTagCount(key, tag));
    }

    public void deleteTags(List<String> tags) {
        tags.forEach(tag -> decreaseTagCount(TAGS_KEY, tag));
    }

    public void deleteQuestionTags(Long questionId, List<String> tags) {
        String key = QUESTION_TAGS_KEY_PREFIX + questionId.toString();
        tags.forEach(tag -> decreaseTagCount(key, tag));
    }

    private void increaseTagCount(String key, String tag) {
        Double count = zSetOperations.score(key, tag);
        if (count == null)
            zSetOperations.add(key, tag, 1.0);
        else
            zSetOperations.incrementScore(key, tag, 1.0);
    }

    private void decreaseTagCount(String key, String tag) {
        Double count = zSetOperations.score(key, tag);
        if (count == 1.0)
            zSetOperations.remove(key, tag);
        else
            zSetOperations.incrementScore(key, tag, 1.0);
    }

    //해당 question 답변에 많이 쓰인 태그 7개 조회
    public Set<String> getTagsByQuestion(Long questionId) {
        String question = questionId.toString();
        return zSetOperations.reverseRange(QUESTION_TAGS_KEY_PREFIX + question, 0, 7);
    }

    //밤 12시 정각이 될때마다 기존의 count를 50%로 줄임
    public void decreaseTagCount() {
        Set<ZSetOperations.TypedTuple<String>> tags = zSetOperations.rangeWithScores(TAGS_KEY, 0, -1);

        tags.forEach(tag -> {
                if(tag.getValue() != null && tag.getScore() != null)
                    zSetOperations.add(TAGS_KEY, tag.getValue(), tag.getScore() * 0.5);
        });
    }
}
