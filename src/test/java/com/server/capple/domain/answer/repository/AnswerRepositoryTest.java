package com.server.capple.domain.answer.repository;

import com.server.capple.domain.answer.entity.Answer;
import com.server.capple.domain.member.entity.Member;
import com.server.capple.domain.member.entity.Role;
import com.server.capple.domain.member.repository.MemberRepository;
import com.server.capple.domain.question.entity.Question;
import com.server.capple.domain.question.entity.QuestionStatus;
import com.server.capple.domain.question.repository.QuestionRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DisplayName("Answer 레포지토리의 ")
@ActiveProfiles("test")
@Transactional
@SpringBootTest
class AnswerRepositoryTest {
    @Autowired
    private AnswerRepository answerRepository;
    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    private QuestionRepository questionRepository;

    @BeforeEach
    void setUp() {
        member1 = Member.builder().role(Role.ROLE_ACADEMIER).nickname("").email("").sub("").build();
        memberRepository.save(member1);

        question1 = Question.builder().content("").questionStatus(QuestionStatus.OLD).build();
        question2 = Question.builder().content("").questionStatus(QuestionStatus.OLD).build();
        question3 = Question.builder().content("").questionStatus(QuestionStatus.OLD).build();
        questionRepository.saveAll(List.of(question1, question2, question3));
    }

    static Member member1;
    static Question question1;
    static Question question2;
    static Question question3;

    @AfterEach
    void tearDown() {
        answerRepository.deleteAllInBatch();
        memberRepository.deleteAllInBatch();
        questionRepository.deleteAllInBatch();
    }

    @DisplayName("사용자가 작성한 답변이 없을 경우 답변의 개수를 0으로 반환한다.")
    @Test
    void getAnswerCountMemberIdZero() {
        // given
        int expected = 0;

        // when
        Integer count = answerRepository.getAnswerCountByMember(member1);

        // then
        assertEquals(expected, count);

    }

    @DisplayName("사용자가 작성한 답변의 개수를 조회한다.")
    @Test
    void getAnswerCountByMemberId() {
        // given
        Answer answer1 = createAnswer(question1);
        Answer answer2 = createAnswer(question2);
        List<Answer> answerList = List.of(answer1, answer2);
        answerRepository.saveAll(answerList);

        // when
        Integer count = answerRepository.getAnswerCountByMember(member1);

        // then
        assertEquals(answerList.size(), count);
    }

    private static Answer createAnswer(Question question1) {
        return Answer.builder()
            .member(member1)
            .question(question1)
            .content("")
            .build();
    }
}