package com.server.capple.domain.tag.service;

import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.data.redis.core.ZSetOperations.TypedTuple;

import java.util.List;
import java.util.Set;

public interface TagService {
    void saveQuestionTags(Long questionId, List<String> tags);
    void saveTags(List<String> tags);
    Set<TypedTuple<String>> getTags(Long questionId);

}
