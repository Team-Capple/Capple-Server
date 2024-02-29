package com.server.capple.domain.tag.service;

import com.server.capple.domain.tag.repository.TagRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.ZSetOperations.TypedTuple;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class TagServiceImpl implements TagService {

    private final TagRepository tagRepository;

    //전체 tag 저장, count update
    @Override
    public void saveTags(List<String> tags) {
        tagRepository.saveTags(tags);
    }

    //질문 별 tag 저장, count update
    @Override
    public void saveQuestionTags(Long questionId, List<String> tags) {
        tagRepository.saveQuestionTags(questionId,tags);
    }


    @Override
    public Set<TypedTuple<String>> getTags(Long questionId) {
        return tagRepository.getTags(questionId);
    }

}
