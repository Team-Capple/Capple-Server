package com.server.capple.domain.answerSubscribeMember.repository;

import com.server.capple.domain.answer.entity.Answer;
import com.server.capple.domain.answer.repository.AnswerRepository;
import com.server.capple.domain.answerSubscribeMember.entity.AnswerSubscribeMember;
import com.server.capple.domain.member.entity.Member;
import com.server.capple.domain.member.repository.MemberRepository;
import com.server.capple.domain.question.entity.Question;
import com.server.capple.domain.question.repository.QuestionRepository;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static com.server.capple.domain.member.entity.Role.ROLE_ACADEMIER;
import static com.server.capple.domain.question.entity.QuestionStatus.LIVE;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.groups.Tuple.tuple;

@ActiveProfiles("test")
@SpringBootTest
@DisplayName("AnswerSubscribeMemberRepository 로 ")
class AnswerSubscribeMemberRepositoryTest {
    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    private QuestionRepository questionRepository;
    @Autowired
    private AnswerRepository answerRepository;
    @Autowired
    private AnswerSubscribeMemberRepository answerSubscribeMemberRepository;

    @Test
    @Transactional
    @DisplayName("사용자가 특정 답변에 댓글을 작성했는지 확인할 수 있다.")
    void existsByMemberIdAndAnswerId() {
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
        Member member3 = Member.builder()
            .nickname("member3")
            .email("member3")
            .sub("member3")
            .role(ROLE_ACADEMIER)
            .build();
        memberRepository.saveAll(List.of(member1, member2, member3));
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
        Boolean result1 = answerSubscribeMemberRepository.existsByMemberIdAndAnswerId(member1.getId(), answer.getId());
        Boolean result2 = answerSubscribeMemberRepository.existsByMemberIdAndAnswerId(member2.getId(), answer.getId());
        Boolean result3 = answerSubscribeMemberRepository.existsByMemberIdAndAnswerId(member3.getId(), answer.getId());

        // then
        assertThat(result1).isTrue();
        assertThat(result2).isTrue();
        assertThat(result3).isFalse();
    }

    @Test
    @Transactional
    @DisplayName("특정 답변의 추가 댓글 구독자를 확인할 수 있다.")
    void findAnswerSubscribeMembersByAnswerId() {
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
        List<AnswerSubscribeMember> result = answerSubscribeMemberRepository.findAnswerSubscribeMembersByAnswerId(answer.getId());

        // then
        assertThat(result).hasSize(2)
            .extracting("answer.id", "member.id")
            .containsExactlyInAnyOrder(
                tuple(answer.getId(), member1.getId()),
                tuple(answer.getId(), member2.getId())
            );
    }

    @Test
    @Transactional
    @DisplayName("특정 답변의 구독자 리스트를 지울 수 있다.")
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
        answerSubscribeMemberRepository.deleteAnswerSubscribeMemberByAnswerId(answer.getId());

        // then
        List<AnswerSubscribeMember> result = answerSubscribeMemberRepository.findAnswerSubscribeMembersByAnswerId(answer.getId());
        assertThat(result).hasSize(0);
    }
}