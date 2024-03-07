package com.server.capple.domain.tag.service;

import com.server.capple.domain.tag.dto.TagResponse;

import java.util.List;

public interface TagService {
    void saveQuestionTags(Long questionId, List<String> tags);

    void saveTags(List<String> tags);

    void deleteQuestionTags(Long questionId, List<String> tags);

    void deleteTags(List<String> tags);

    void updateQuestionTags(Long questionId, List<String> addedTags, List<String> removedTags);

    void updateTags(List<String> addedTags, List<String> removedTags);

    TagResponse.TagInfos getTagsByQuestion(Long questionId);

    void findOrCreateTag(String tagName);

    TagResponse.TagInfos searchTags(String keyword);
}
