package com.server.capple.domain.answer.service;

import com.server.capple.domain.answer.dto.AnswerRequest;
import com.server.capple.domain.answer.entity.Answer;
import com.server.capple.domain.member.entity.Member;
import com.server.capple.domain.member.repository.MemberRepository;
import com.server.capple.domain.question.entity.Question;
import com.server.capple.domain.question.entity.QuestionStatus;
import com.server.capple.domain.question.repository.QuestionRepository;
import com.server.capple.domain.tag.dto.TagResponse;
import com.server.capple.domain.tag.service.TagService;
import com.server.capple.support.ServiceTestConfig;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.ZSetOperations.TypedTuple;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DisplayName("Member 서비스의 ")
@SpringBootTest
public class AnswerServiceTest extends ServiceTestConfig {


    @Test
    @DisplayName("Answer 생성 테스트")
    @Transactional
    public void createAnswer() {
        //given
        AnswerRequest request = getAnswerRequest();

        //when
        Long answerId = answerService.createAnswer(member,question.getId(),request).getAnswerId();
        Answer answer = answerService.findAnswer(answerId);
        Set<TypedTuple<String>> tags = tagService.getTags(question.getId());

        //then
        assertEquals("나는 와플을 좋아하는 사람이 좋아", answer.getContent());
        assertEquals("#와플유니버시티 #와플",answer.getTags());
        assertEquals(2, tags.size());
        for (TypedTuple<String> tuple : tags) {
            System.out.println(tuple.getValue() + " " + tuple.getScore());
        }
    }

}
