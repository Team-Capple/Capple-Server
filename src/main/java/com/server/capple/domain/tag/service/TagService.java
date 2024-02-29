package com.server.capple.domain.tag.service;

import com.server.capple.domain.tag.dto.TagResponse;
import com.server.capple.domain.tag.entity.Tag;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.data.redis.core.ZSetOperations.TypedTuple;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface TagService {
    void saveQuestionTags(Long questionId, List<String> tags);
    void saveTags(List<String> tags);
    TagResponse.TagInfos getTags(Long questionId);
    void findOrCreateTag(String tagName);
    TagResponse.TagInfos searchTags(String keyword);

}
