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
        tags.forEach(tag -> {
            Double count = zSetOperations.score("tags", tag);
            if (count == null)
                zSetOperations.add("tags", tag, 1.0);
            else
                zSetOperations.incrementScore("tags", tag, 1.0);
        });
    }

    //질문에 따른 tag 저장, tagCount update
    public void saveQuestionTags(Long questionId, List<String> tags) {
        String question = questionId.toString();

        tags.forEach(tag -> {
            Double count = zSetOperations.score(question, tag);
            if (count == null)
                zSetOperations.add(question, tag, 1.0);
            else
                zSetOperations.incrementScore(question, tag, 1.0);
        });
    }

    //해당 question 답변에 많이 쓰인 태그 조회
    public Set<String> getTagsByQuestion(Long questionId) {
        String question = questionId.toString();
        return zSetOperations.reverseRange(question, 0, -1);
    }


}
