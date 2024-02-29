package com.server.capple.domain.tag.service;

import com.server.capple.domain.answer.dto.AnswerRequest;
import com.server.capple.domain.tag.dto.TagResponse;
import com.server.capple.support.ServiceTestConfig;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TagServiceTest extends ServiceTestConfig {
    @Test
    @DisplayName("Tag 검색 테스트")
    @Transactional
    public void searchTags() {
        //given
        String keyword = "와플";
        AnswerRequest request = getAnswerRequest();
        answerService.createAnswer(member,question.getId(),request);

        //when
        TagResponse.TagInfos tags = tagService.searchTags(keyword);

        assertEquals(2, tags.getTags().size());
    }



}
