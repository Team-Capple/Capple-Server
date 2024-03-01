package com.server.capple.domain.tag.service;

import com.server.capple.domain.tag.dto.TagResponse;
import com.server.capple.domain.tag.entity.Tag;
import com.server.capple.domain.tag.mapper.TagMapper;
import com.server.capple.domain.tag.repository.TagRedisRepository;
import com.server.capple.domain.tag.repository.TagRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class TagServiceImpl implements TagService {

    private final TagRedisRepository tagRedisRepository;
    private final TagRepository tagRepository;
    private final TagMapper tagMapper;

    //전체 tag 저장, count update
    @Override
    public void saveTags(List<String> tags) {
        tagRedisRepository.saveTags(tags);
    }

    //질문 별 tag 저장, count update
    @Override
    public void saveQuestionTags(Long questionId, List<String> tags) {
        tagRedisRepository.saveQuestionTags(questionId, tags);
    }

    @Override
    public TagResponse.TagInfos getTagsByQuestion(Long questionId) {
        Set<String> typedTuples = tagRedisRepository.getTagsByQuestion(questionId);
        return new TagResponse.TagInfos(new ArrayList<>(typedTuples));
    }


    @Override
    @Transactional
    public void findOrCreateTag(String tagName) {
        findTagByTagName(tagName)
                .orElseGet(() -> tagRepository.save(Tag.builder().tagName(tagName).build()));
    }

    @Override
    public TagResponse.TagInfos searchTags(String keyword) {
        return tagMapper.toTagInfos(tagRepository.findTagsByKeyword(keyword));
    }

    private Optional<Tag> findTagByTagName(String tagName) {
        return tagRepository.findByTagName(tagName);
    }
}
