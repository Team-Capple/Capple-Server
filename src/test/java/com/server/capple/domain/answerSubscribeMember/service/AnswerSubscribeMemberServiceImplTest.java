package com.server.capple.domain.answerSubscribeMember.service;

import com.server.capple.domain.answer.entity.Answer;
import com.server.capple.domain.answer.repository.AnswerRepository;
import com.server.capple.domain.answerSubscribeMember.entity.AnswerSubscribeMember;
import com.server.capple.domain.answerSubscribeMember.repository.AnswerSubscribeMemberRepository;
import com.server.capple.domain.member.entity.Member;
import com.server.capple.domain.member.repository.MemberRepository;
import com.server.capple.domain.question.entity.Question;
import com.server.capple.domain.question.repository.QuestionRepository;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ActiveProfiles;

import java.util.Collection;
import java.util.List;

import static com.server.capple.domain.member.entity.Role.ROLE_ACADEMIER;
import static com.server.capple.domain.question.entity.QuestionStatus.LIVE;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;
import static org.mockito.Mockito.*;

@ActiveProfiles("test")
@SpringBootTest
@DisplayName("AnswerSubscribeMemberServiceImpl 로 ")
class AnswerSubscribeMemberServiceImplTest {
    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    private QuestionRepository questionRepository;
    @Autowired
    private AnswerRepository answerRepository;
    @SpyBean
    private AnswerSubscribeMemberRepository answerSubscribeMemberRepository;
    @Autowired
    private AnswerSubscribeMemberServiceImpl answerSubscribeMemberService;
    @Autowired
    private JdbcTemplate jdbcTemplate;

    @AfterEach
    void tearDown() {
        jdbcTemplate.execute("DELETE FROM answer_subscribe_member");
        jdbcTemplate.execute("DELETE FROM answer_comment");
        jdbcTemplate.execute("DELETE FROM answer_heart");
        jdbcTemplate.execute("DELETE FROM answer");
        jdbcTemplate.execute("DELETE FROM question");
        jdbcTemplate.execute("DELETE FROM member");
    }

    @DisplayName("답변의 댓글의 구독자를 추가할 때 ")
    @TestFactory
    Collection<DynamicTest> createAnswerSubscribeMember() {
        // given
        Member member1 = Member.builder()
            .nickname("member1")
            .email("member1")
            .sub("member1")
            .role(ROLE_ACADEMIER)
            .build();
        Member member2 = Member.builder()
            .nickname("member2")
            .email("member2")
            .sub("member2")
            .role(ROLE_ACADEMIER)
            .build();
        memberRepository.saveAll(List.of(member1, member2));
        Question question = Question.builder()
            .content("question")
            .questionStatus(LIVE)
            .build();
        questionRepository.save(question);
        Answer answer = Answer.builder()
            .content("answer")
            .question(question)
            .member(member1)
            .build();
        answerRepository.save(answer);


        return List.of(
            DynamicTest.dynamicTest("기존에 존재하지 않는 경우 추가한다.", () -> {
                // when
                answerSubscribeMemberService.createAnswerSubscribeMember(member2, answer);

                // then
                verify(answerSubscribeMemberRepository, times(1)).save(any(AnswerSubscribeMember.class));
            }),
            DynamicTest.dynamicTest("기존에 존재하는 경우 추가 하지 않는다.", () -> {
                // when
                answerSubscribeMemberService.createAnswerSubscribeMember(member2, answer);

                // then
                verify(answerSubscribeMemberRepository, times(1)).save(any(AnswerSubscribeMember.class));
            })
        );
    }

    @Test
    @DisplayName("답변의 구독자를 Member 엔티티로 조회할 수 있다.")
    @Transactional
    void findAnswerSubscribeMembers() {
        // given
        Member member1 = Member.builder()
            .nickname("member1")
            .email("member1")
            .sub("member1")
            .role(ROLE_ACADEMIER)
            .build();
        Member member2 = Member.builder()
            .nickname("member2")
            .email("member2")
            .sub("member2")
            .role(ROLE_ACADEMIER)
            .build();
        memberRepository.saveAll(List.of(member1, member2));
        Question question = Question.builder()
            .content("question")
            .questionStatus(LIVE)
            .build();
        questionRepository.save(question);
        Answer answer = Answer.builder()
            .content("answer")
            .question(question)
            .member(member1)
            .build();
        answerRepository.save(answer);
        AnswerSubscribeMember answerSubscribeMember1 = AnswerSubscribeMember.builder()
            .answer(answer)
            .member(member1)
            .build();
        AnswerSubscribeMember answerSubscribeMember2 = AnswerSubscribeMember.builder()
            .answer(answer)
            .member(member2)
            .build();
        answerSubscribeMemberRepository.saveAll(List.of(answerSubscribeMember1, answerSubscribeMember2));

        // when
        List<Member> result = answerSubscribeMemberService.findAnswerSubscribeMembers(answer.getId());

        // then
        assertThat(result).hasSize(2)
            .extracting("id", "nickname")
            .containsExactlyInAnyOrder(
                tuple(member1.getId(), member1.getNickname()),
                tuple(member2.getId(), member2.getNickname())
            );
    }

    @Test
    @DisplayName("답변의 구독자 리스트를 제거할 수 있다.")
    @Transactional
    void deleteAnswerSubscribeMemberByAnswerId() {
        // given
        Member member1 = Member.builder()
            .nickname("member1")
            .email("member1")
            .sub("member1")
            .role(ROLE_ACADEMIER)
            .build();
        Member member2 = Member.builder()
            .nickname("member2")
            .email("member2")
            .sub("member2")
            .role(ROLE_ACADEMIER)
            .build();
        memberRepository.saveAll(List.of(member1, member2));
        Question question = Question.builder()
            .content("question")
            .questionStatus(LIVE)
            .build();
        questionRepository.save(question);
        Answer answer = Answer.builder()
            .content("answer")
            .question(question)
            .member(member1)
            .build();
        answerRepository.save(answer);
        AnswerSubscribeMember answerSubscribeMember1 = AnswerSubscribeMember.builder()
            .answer(answer)
            .member(member1)
            .build();
        AnswerSubscribeMember answerSubscribeMember2 = AnswerSubscribeMember.builder()
            .answer(answer)
            .member(member2)
            .build();
        answerSubscribeMemberRepository.saveAll(List.of(answerSubscribeMember1, answerSubscribeMember2));

        // when
        answerSubscribeMemberService.deleteAnswerSubscribeMemberByAnswerId(answer.getId());

        // then
        List<Member> result = answerSubscribeMemberService.findAnswerSubscribeMembers(answer.getId());
        assertThat(result).hasSize(0);
    }
}