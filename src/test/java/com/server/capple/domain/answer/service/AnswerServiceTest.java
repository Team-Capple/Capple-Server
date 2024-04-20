package com.server.capple.domain.answer.service;

import com.server.capple.domain.answer.dto.AnswerRequest;
import com.server.capple.domain.answer.dto.AnswerResponse;
import com.server.capple.domain.answer.entity.Answer;
import com.server.capple.domain.tag.service.TagService;
import com.server.capple.support.ServiceTestConfig;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Answer 서비스의 ")
@SpringBootTest
public class AnswerServiceTest extends ServiceTestConfig {
    @Autowired
    private AnswerService answerService;
    @Autowired
    private TagService tagService;

    @Test
    @DisplayName("Answer 생성 테스트")
    @Transactional
    public void createAnswerTest() {
        //given
        AnswerRequest request = getAnswerRequest();

        //when
        Long answerId = answerService.createAnswer(member, liveQuestion.getId(), request).getAnswerId();
        Answer answer = answerService.findAnswer(answerId);
        List<String> tags = tagService.getTagsByQuestion(liveQuestion.getId(), 7).getTags();

        //then
        assertEquals("나는 와플을 좋아하는 사람이 좋아", answer.getContent());
        assertEquals("#와플유니버시티 #와플 ", answer.getTags());
        assertEquals(2, tags.size());
        assertTrue(tags.contains("#와플"));
        assertTrue(tags.contains("#와플유니버시티"));

    }

    @Test
    @DisplayName("Answer 수정 테스트")
    @Transactional
    public void updateAnswerTest() {
        //given
        AnswerRequest request = getAnswerRequest();
        Long answerId = answerService.createAnswer(member, liveQuestion.getId(), request).getAnswerId();

        AnswerRequest updateRequest = AnswerRequest.builder()
                .answer("나는 동그랗고 와플 좋아하는 사람이 좋아")
                .tags(List.of("#동그라미", "#와플", "#동글"))
                .build();

        //when
        answerService.updateAnswer(member, answerId, updateRequest);
        Answer answer = answerService.findAnswer(answerId);

        //then
        assertEquals("나는 동그랗고 와플 좋아하는 사람이 좋아", answer.getContent());
        assertEquals("#동그라미 #와플 #동글 ", answer.getTags());
        List<String> tags = tagService.getTagsByQuestion(liveQuestion.getId(), 7).getTags();

        assertEquals(3, tags.size());
        assertTrue(tags.contains("#와플"));
        assertTrue(tags.contains("#동그라미"));
        assertFalse(tags.contains("#와플유니버시티"));

    }

    @Test
    @DisplayName("Answer 삭제 테스트")
    @Transactional
    public void deleteAnswerTest() {
        //given
        AnswerRequest request = getAnswerRequest();
        Long answerId = answerService.createAnswer(member, liveQuestion.getId(), request).getAnswerId();

        //when
        answerService.deleteAnswer(member, answerId);
        Answer answer = answerService.findAnswer(answerId);

        //then
        List<String> tags = tagService.getTagsByQuestion(liveQuestion.getId(), 7).getTags();

        assertEquals(0, tags.size());
        assertNotNull(answer.getDeletedAt());

    }

    @Test
    @DisplayName("Answer 좋아요/취소 테스트")
    @Transactional
    public void toggleAnswerHeartTest() {
        //when
        AnswerResponse.AnswerLike answerLike = answerService.toggleAnswerHeart(member, answer.getId());
        //then
        assertEquals(Boolean.TRUE, answerLike.getIsLiked());

        //when
        AnswerResponse.AnswerLike answerLike2 = answerService.toggleAnswerHeart(member, answer.getId());
        //then
        assertEquals(Boolean.FALSE, answerLike2.getIsLiked());

    }
}
