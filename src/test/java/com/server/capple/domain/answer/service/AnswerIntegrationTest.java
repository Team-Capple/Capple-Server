package com.server.capple.domain.answer.service;

import com.server.capple.domain.answer.dto.AnswerRequest;
import com.server.capple.domain.answer.entity.Answer;
import com.server.capple.domain.member.entity.Member;
import com.server.capple.domain.member.repository.MemberRepository;
import com.server.capple.domain.question.entity.Question;
import com.server.capple.domain.question.entity.QuestionStatus;
import com.server.capple.domain.question.repository.QuestionRepository;
import com.server.capple.domain.tag.service.TagService;
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
public class AnswerIntegrationTest {
    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    private QuestionRepository questionRepository;
    @Autowired
    private AnswerService answerService;
    @Autowired
    private TagService tagService;
    private Member member;
    private Question question;


    @BeforeEach
    public void setUp() {
        member = createMember();
        question = createQuestion();
    }

    private Member createMember() {
        return memberRepository.save(
                Member.builder()
                        .email("tnals2384@gmail.com")
                        .nickname("루시")
                        .profileImage("lucy.jpg")
                        .build()
        );
    }

    private Question createQuestion() {
        return questionRepository.save(
                Question.builder()
                        .content("아카데미 러너 중 가장 마음에 드는 유형이 있나요?")
                        .questionStatus(QuestionStatus.ONGOING)
                        .build()
        );
    }

    private AnswerRequest getAnswerRequest() {
        return AnswerRequest.builder()
                .answer("나는 무자비한 사람이 좋아")
                .keywords(List.of("#무자비", "#당근맨"))
                .build();
    }

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
        assertEquals("나는 무자비한 사람이 좋아", answer.getContent());
        assertEquals("#무자비 #당근맨",answer.getTags());
        assertEquals(2, tags.size());
        for (TypedTuple<String> tuple : tags) {
            System.out.println(tuple.getValue() + " " + tuple.getScore());
        }
    }




}
