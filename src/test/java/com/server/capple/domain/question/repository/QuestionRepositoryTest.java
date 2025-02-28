package com.server.capple.domain.question.repository;

import com.server.capple.domain.answer.entity.Answer;
import com.server.capple.domain.answer.repository.AnswerRepository;
import com.server.capple.domain.member.entity.Member;
import com.server.capple.domain.member.entity.Role;
import com.server.capple.domain.member.repository.MemberRepository;
import com.server.capple.domain.question.dao.QuestionInfoInterface;
import com.server.capple.domain.question.entity.Question;
import com.server.capple.domain.question.entity.QuestionStatus;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@DisplayName("Question 레포지토리의 ")
@ActiveProfiles("test")
@Transactional
@SpringBootTest
class QuestionRepositoryTest {
    @Autowired
    private QuestionRepository questionRepository;
    @Autowired
    private AnswerRepository answerRepository;
    @Autowired
    private MemberRepository memberRepository;

    @BeforeEach
    void setUp() {
        member1 = Member.builder().role(Role.ROLE_ACADEMIER).nickname("사용자1").email("").sub("").build();
        member2 = Member.builder().role(Role.ROLE_ACADEMIER).nickname("사용자2").email("").sub("").build();
        memberRepository.saveAll(List.of(member1, member2));

        questions.add(Question.builder().content("질문1").livedAt(LocalDateTime.now().minusDays(7)).questionStatus(QuestionStatus.OLD).build());
        questions.add(Question.builder().content("질문2").livedAt(LocalDateTime.now().minusDays(6)).questionStatus(QuestionStatus.OLD).build());
        questions.add(Question.builder().content("질문3").livedAt(LocalDateTime.now().minusDays(5)).questionStatus(QuestionStatus.OLD).build());
        questions.add(Question.builder().content("질문4").livedAt(LocalDateTime.now().minusDays(4)).questionStatus(QuestionStatus.OLD).build());
        questions.add(Question.builder().content("질문5").livedAt(LocalDateTime.now().minusDays(3)).questionStatus(QuestionStatus.OLD).build());
        questions.add(Question.builder().content("질문6").livedAt(LocalDateTime.now().minusDays(2)).questionStatus(QuestionStatus.OLD).build());
        questions.add(Question.builder().content("질문7").livedAt(LocalDateTime.now().minusHours(1)).questionStatus(QuestionStatus.LIVE).build());
        questionRepository.saveAll(questions);
    }

    Member member1;
    Member member2;
    List<Question> questions = new ArrayList<>();

    @DisplayName("사용자가 답변한 질문을 조회한다.")
    @TestFactory
    Collection<DynamicTest> findAnswerdQuestionsByLivedAtBefore() {
        // given
        answerRepository.save(createAnswer(member1, questions.get(0)));
        answerRepository.save(createAnswer(member1, questions.get(1)));
        answerRepository.save(createAnswer(member1, questions.get(2)));
        answerRepository.save(createAnswer(member1, questions.get(5)));
        answerRepository.save(createAnswer(member1, questions.get(6)));
        answerRepository.save(createAnswer(member2, questions.get(1)));
        answerRepository.save(createAnswer(member2, questions.get(2)));

        return List.of(
            DynamicTest.dynamicTest("첫 페이지 조회", () -> {
                // when
                Slice<QuestionInfoInterface> returnedQuestions = questionRepository.findAnswerdQuestionsByLivedAtBefore(
                    member1,
                    LocalDateTime.now(),
                    PageRequest.of(0, 3, Sort.by(Sort.Direction.DESC, "livedAt"))
                );

                // then
                assertThat(returnedQuestions.getContent()).hasSize(3)
                    .extracting("question.content", "question.questionStatus", "isAnsweredByMember")
                    .containsExactlyInAnyOrder(
                        tuple(questions.get(6).getContent(), questions.get(6).getQuestionStatus(), true),
                        tuple(questions.get(5).getContent(), questions.get(5).getQuestionStatus(), true),
                        tuple(questions.get(2).getContent(), questions.get(2).getQuestionStatus(), true)
                    );
            }),
            DynamicTest.dynamicTest("마지막 페이지 조회", () -> {
                // when
                Slice<QuestionInfoInterface> returnedQuestions = questionRepository.findAnswerdQuestionsByLivedAtBefore(
                    member1,
                    questions.get(2).getLivedAt(),
                    PageRequest.of(0, 3, Sort.by(Sort.Direction.DESC, "livedAt"))
                );

                // then
                assertThat(returnedQuestions.getContent()).hasSize(2)
                    .extracting("question.content", "question.questionStatus", "isAnsweredByMember")
                    .containsExactlyInAnyOrder(
                        tuple(questions.get(1).getContent(), questions.get(1).getQuestionStatus(), true),
                        tuple(questions.get(0).getContent(), questions.get(0).getQuestionStatus(), true)
                    );
            })
        );
    }

    private Answer createAnswer(Member member, Question question) {
        return Answer.builder()
            .member(member)
            .question(question)
            .content("content")
            .build();
    }
}