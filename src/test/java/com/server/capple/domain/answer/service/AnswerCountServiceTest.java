package com.server.capple.domain.answer.service;

import com.server.capple.domain.answer.entity.Answer;
import com.server.capple.domain.answer.repository.AnswerRepository;
import com.server.capple.domain.member.entity.Member;
import com.server.capple.domain.member.entity.Role;
import com.server.capple.domain.member.repository.MemberRepository;
import com.server.capple.domain.question.entity.Question;
import com.server.capple.domain.question.entity.QuestionStatus;
import com.server.capple.domain.question.repository.QuestionRepository;
import com.server.capple.support.ServiceTestConfig;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("AnswerCount 서비스의 ")
@ActiveProfiles("test")
@SpringBootTest
class AnswerCountServiceTest {
    @Autowired
    private AnswerRepository answerRepository;
    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    private QuestionRepository questionRepository;
    @Autowired
    private AnswerCountService answerCountService;
    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @BeforeEach
    void setup() {
        member = Member.builder()
            .nickname("").email("").role(Role.ROLE_ACADEMIER).sub("")
            .build();
        memberRepository.save(member);
        liveQuestion = Question.builder()
            .content("").questionStatus(QuestionStatus.LIVE)
            .build();
        oldQuestion1 = Question.builder()
            .content("").questionStatus(QuestionStatus.OLD)
            .build();
        oldQuestion2 = Question.builder()
            .content("").questionStatus(QuestionStatus.OLD)
            .build();
        questionRepository.saveAll(List.of(liveQuestion,oldQuestion1,oldQuestion2));
    }

    @AfterEach
    void tearDown() {
        answerRepository.deleteAllInBatch();
        memberRepository.deleteAllInBatch();
        questionRepository.deleteAllInBatch();
        redisTemplate.delete(String.format("answerCountByMember::%d", member.getId()));
    }

    private static Member member;
    private static Question liveQuestion;
    private static Question oldQuestion1;
    private static Question oldQuestion2;

    @DisplayName("사용자가 작성한 답변의 개수를 조회한다.")
    @Test
    void getAnswerCountByMember() {
        // given
        Answer answer1 = createAnswer(liveQuestion);
        Answer answer2 = createAnswer(oldQuestion1);
        List<Answer> answerList = List.of(answer1, answer2);
        answerRepository.saveAll(answerList);

        // when
        Integer count = answerCountService.getAnswerCountByMember(member);

        // then
        assertEquals(answerList.size(), count);
    }

    @DisplayName("사용자가 작성한 답변의 개수가 메서드 사용시 캐싱된다.")
    @Test
    void getAnswerCountByMemberIsCacheable() {
        // given
        Answer answer1 = createAnswer(liveQuestion);
        Answer answer2 = createAnswer(oldQuestion1);
        List<Answer> answerList = List.of(answer1, answer2);
        answerRepository.saveAll(answerList);

        answerCountService.getAnswerCountByMember(member);

        // when
        Integer count = (Integer) redisTemplate.opsForValue().get(String.format("answerCountByMember::%d", member.getId()));

        // then
        assertEquals(answerList.size(), count);
    }


    @DisplayName("사용자가 작성한 답변의 개수의 캐시를 만료시킬 수 있다.")
    @Test
    void getAnswerCountByMemberIsCacheEvictable() {
        // given
        Answer answer1 = createAnswer(liveQuestion);
        Answer answer2 = createAnswer(oldQuestion1);
        List<Answer> answerList = List.of(answer1, answer2);
        answerRepository.saveAll(answerList);

        answerCountService.getAnswerCountByMember(member);
        answerCountService.expireMembersAnsweredCount(member);


        // when
        Object count = redisTemplate.opsForValue().get(String.format("answerCountByMember::%d", member.getId()));

        // then
        assertNull(count);
    }

    private Answer createAnswer(Question liveQuestion1) {
        return Answer.builder()
            .member(member)
            .question(liveQuestion1)
            .content("")
            .build();
    }
}