package com.server.capple.domain.questionSubcribeMember.repository;

import com.server.capple.domain.member.entity.Member;
import com.server.capple.domain.member.repository.MemberRepository;
import com.server.capple.domain.question.entity.Question;
import com.server.capple.domain.question.repository.QuestionRepository;
import com.server.capple.domain.questionSubcribeMember.entity.QuestionSubscribeMember;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;
import java.util.Objects;

import static com.server.capple.domain.member.entity.Role.ROLE_ACADEMIER;
import static com.server.capple.domain.question.entity.QuestionStatus.LIVE;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;

@ActiveProfiles("test")
@SpringBootTest
@DisplayName("question subscribe member 리포지토리로 ")
class QuestionSubscribeMemberRepositoryTest {
    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    private QuestionRepository questionRepository;
    @Autowired
    private QuestionSubscribeMemberRepository questionSubscribeMemberRepository;

    @AfterEach
    void tearDown() {
        questionSubscribeMemberRepository.deleteAllInBatch();
        questionRepository.deleteAllInBatch();
        memberRepository.deleteAllInBatch();
    }

    @Test
    @DisplayName("question의 구독자를 제거할수 있다.")
    @Transactional
    void deleteByQuestion() {
        // given
        Question question = Question.builder()
            .content("question1")
            .questionStatus(LIVE)
            .build();
        Question savedQuestion = questionRepository.save(question);
        Member member1 = Member.builder()
            .nickname("member1")
            .email("member1")
            .sub("member1")
            .role(ROLE_ACADEMIER)
            .build();
        member1 = memberRepository.save(member1);
        Member member2 = Member.builder()
            .nickname("member2")
            .email("member2")
            .sub("member2")
            .role(ROLE_ACADEMIER)
            .build();
        member2 = memberRepository.save(member2);
        QuestionSubscribeMember questionSubscribeMember1 = QuestionSubscribeMember.builder()
            .question(savedQuestion)
            .member(member1)
            .build();
        QuestionSubscribeMember questionSubscribeMember2 = QuestionSubscribeMember.builder()
            .question(savedQuestion)
            .member(member2)
            .build();
        questionSubscribeMemberRepository.saveAll(List.of(questionSubscribeMember1, questionSubscribeMember2));

        // when
        questionSubscribeMemberRepository.deleteQuestionSubscribeMemberByQuestion(savedQuestion);

        // then
        List<QuestionSubscribeMember> result = questionSubscribeMemberRepository.findAll().stream().filter(subsciber -> Objects.equals(subsciber.getQuestion().getId(), savedQuestion.getId())).toList();
        assertThat(result).isEmpty();
    }

    @Test
    @DisplayName("question의 구독자를 찾을 수 있다.")
    void getSubscriberByQuestion() {
        // given
        Question question = Question.builder()
            .content("question1")
            .questionStatus(LIVE)
            .build();
        Question savedQuestion = questionRepository.save(question);
        Member member1 = Member.builder()
            .nickname("member1")
            .email("member1")
            .sub("member1")
            .role(ROLE_ACADEMIER)
            .build();
        member1 = memberRepository.save(member1);
        Member member2 = Member.builder()
            .nickname("member2")
            .email("member2")
            .sub("member2")
            .role(ROLE_ACADEMIER)
            .build();
        member2 = memberRepository.save(member2);
        QuestionSubscribeMember questionSubscribeMember1 = QuestionSubscribeMember.builder()
            .question(savedQuestion)
            .member(member1)
            .build();
        QuestionSubscribeMember questionSubscribeMember2 = QuestionSubscribeMember.builder()
            .question(savedQuestion)
            .member(member2)
            .build();
        questionSubscribeMemberRepository.saveAll(List.of(questionSubscribeMember1, questionSubscribeMember2));

        // when
        List<QuestionSubscribeMember> subscribers = questionSubscribeMemberRepository.getQuestionSubscribeMemberByQuestion(savedQuestion);

        // then
        assertThat(subscribers).hasSize(2)
            .extracting("question.content", "member.nickname")
            .containsExactlyInAnyOrder(
                tuple(savedQuestion.getContent(), member1.getNickname()),
                tuple(savedQuestion.getContent(), member2.getNickname())
            );
    }
}