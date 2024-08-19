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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

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

        //then
        assertEquals("나는 와플을 좋아하는 사람이 좋아", answer.getContent());

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
                .build();

        //when
        answerService.updateAnswer(member, answerId, updateRequest);
        Answer answer = answerService.findAnswer(answerId);

        //then
        assertEquals("나는 동그랗고 와플 좋아하는 사람이 좋아", answer.getContent());
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

    @Test
    @DisplayName("작성한 Answer 목록 조회 테스트")
    @Transactional
    public void getMemberAnswerTest() {
        //when
        AnswerResponse.MemberAnswerList memberAnswer = answerService.getMemberAnswer(member);
        //then
        assertEquals(memberAnswer.getMemberAnswerInfos().get(0).getContent(), "나는 무자비한 사람이 좋아");
    }

    @Test
    @DisplayName("좋아한 Answer 목록 조회 테스트")
    @Transactional
    public void getMemberHeartAnswerTest() {
        //given
        answerService.toggleAnswerHeart(member, answer.getId());

        //when
        AnswerResponse.MemberAnswerList memberHeartAnswer = answerService.getMemberHeartAnswer(member);

        //then
        assertEquals(memberHeartAnswer.getMemberAnswerInfos().get(0).getHeartCount(), 1);
    }
}
