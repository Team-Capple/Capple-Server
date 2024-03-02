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
    @Resource(name = "redisTemplate")
    private ZSetOperations<String, String> zSetOperations;

    //지금 뜨는 키워드 조회를 위한 tag 저장, tagCount update
    public void saveTags(List<String> tags) {
        String key = "tags";
        tags.forEach(tag -> updateTagCount(key, tag));
    }

    //질문에 따른 tag 저장, tagCount update
    public void saveQuestionTags(Long questionId, List<String> tags) {
        String key = "questionTag-"+ questionId.toString();
        tags.forEach(tag -> updateTagCount(key, tag));
    }

    private void updateTagCount(String key, String tag) {
        Double count = zSetOperations.score(key, tag);
        if (count == null)
            zSetOperations.add(key, tag, 1.0);
        else
            zSetOperations.incrementScore(key, tag, 1.0);
    }

    //해당 question 답변에 많이 쓰인 태그 7개 조회
    public Set<String> getTagsByQuestion(Long questionId) {
        String question = questionId.toString();
        return zSetOperations.reverseRange(question, 0, 7);
    }


}
