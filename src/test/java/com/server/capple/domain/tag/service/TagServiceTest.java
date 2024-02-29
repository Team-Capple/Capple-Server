package com.server.capple.domain.tag.service;

import com.server.capple.domain.answer.dto.AnswerRequest;
import com.server.capple.domain.tag.dto.TagResponse;
import com.server.capple.support.ServiceTestConfig;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

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

    @Test
    @DisplayName("이 질문 답변에 사람들이 많이 쓴 키워드 조회 테스트")
    @Transactional
    public void getPopularTags() {
        //given
        AnswerRequest request = getAnswerRequest();
        AnswerRequest request2 = AnswerRequest.builder()
                .answer("나는 와플이랑 바나나를 좋아하는 사람이 좋아")
                .tags(List.of("#바나나", "#와플", "바나나와플"))
                .build();

        //when
        answerService.createAnswer(member,question.getId(),request);
        answerService.createAnswer(member, question.getId(),request2);
        TagResponse.TagInfos tags = tagService.getTagsByQuestion(question.getId());

        //then
        assertEquals("#와플", tags.getTags().get(0));

    }



}
