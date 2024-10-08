package com.server.capple.domain.tag.service;

import com.server.capple.domain.question.service.QuestionService;
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

    //질문 사용된 tags list 조회 (count 높은 순으로)
    @Override
    public TagResponse.TagInfos getTagsByQuestion(Long questionId, int size) {
        Set<String> typedTuples = tagRedisRepository.getTagsByQuestion(questionId, size);
        return new TagResponse.TagInfos(new ArrayList<>(typedTuples));
    }


    //인기 태그 조회
    @Override
    public TagResponse.TagInfos getPopularTags() {
        Set<String> typedTuples = tagRedisRepository.getPopularTags();
        return new TagResponse.TagInfos(new ArrayList<>(typedTuples));
    }

    //answer 삭제시 tag 삭제, count decrease
    @Override
    public void deleteTags(List<String> tags) {
        tagRedisRepository.deleteTags(tags);
    }

    //answer 삭제시 tag 삭제, count decrease
    @Override
    public void deleteQuestionTags(Long questionId, List<String> tags) {
        tagRedisRepository.deleteQuestionTags(questionId, tags);
    }

    @Override
    public void updateTags(List<String> addedTags, List<String> removedTags) {
        saveTags(addedTags);
        deleteTags(removedTags);
    }

    @Override
    public void updateQuestionTags(Long questionId, List<String> addedTags, List<String> removedTags) {
        saveQuestionTags(questionId, addedTags);
        deleteQuestionTags(questionId, removedTags);
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
