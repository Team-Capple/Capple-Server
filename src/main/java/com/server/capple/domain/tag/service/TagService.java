package com.server.capple.domain.tag.service;

import com.server.capple.domain.tag.dto.TagResponse;

import java.util.List;

public interface TagService {
    void saveQuestionTags(Long questionId, List<String> tags);

    void saveTags(List<String> tags);

    TagResponse.TagInfos getTagsByQuestion(Long questionId);

    void findOrCreateTag(String tagName);

    TagResponse.TagInfos searchTags(String keyword);

}
