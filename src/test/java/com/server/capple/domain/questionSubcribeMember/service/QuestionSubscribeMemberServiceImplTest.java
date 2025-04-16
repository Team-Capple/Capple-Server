package com.server.capple.domain.questionSubcribeMember.service;

import com.server.capple.domain.answer.entity.Answer;
import com.server.capple.domain.member.entity.Member;
import com.server.capple.domain.notifiaction.service.NotificationService;
import com.server.capple.domain.question.entity.Question;
import com.server.capple.domain.questionSubcribeMember.entity.QuestionSubscribeMember;
import com.server.capple.domain.questionSubcribeMember.repository.QuestionSubscribeMemberRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import static com.server.capple.domain.member.entity.Role.ROLE_ACADEMIER;
import static com.server.capple.domain.question.entity.QuestionStatus.LIVE;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ActiveProfiles("test")
@ExtendWith(MockitoExtension.class)
@DisplayName("question subscribe member 서비스로 ")
class QuestionSubscribeMemberServiceImplTest {
    @Mock
    private QuestionSubscribeMemberRepository questionSubscribeMemberRepository;

    @Mock
    private NotificationService notificationService;

    @InjectMocks
    private QuestionSubscribeMemberServiceImpl questionSubscribeMemberService;

    @Test
    @DisplayName("라이브 질문 구독자 리스트에 없는경우 추가한다.")
    void addSubscribeMember() throws ExecutionException, InterruptedException {
        // given
        Member member1 = Member.builder()
            .id(1L)
            .nickname("member1")
            .email("member1")
            .sub("member1")
            .role(ROLE_ACADEMIER)
            .build();
        Question question = Question.builder()
            .id(1L)
            .content("question1")
            .questionStatus(LIVE)
            .build();
        Answer answer = Answer.builder()
            .id(1L)
            .question(question)
            .member(member1)
            .content("answer")
            .build();
        QuestionSubscribeMember questionSubscribeMember1 = QuestionSubscribeMember.builder()
            .id(1L)
            .question(question)
            .member(member1)
            .build();

        // When
        doReturn(List.of()).when(questionSubscribeMemberRepository).getQuestionSubscribeMemberByQuestion(any());
        doReturn(questionSubscribeMember1).when(questionSubscribeMemberRepository).save(any());
        doNothing().when(notificationService).sendLiveAnswerAddedNotification(any(),any(),any());
        CompletableFuture<Void> voidCompletableFuture = questionSubscribeMemberService.addMemberAndSendLiveAnswerAddedNotification(member1, question, answer);

        // Then
        voidCompletableFuture.get();
        verify(questionSubscribeMemberRepository, times(1)).save(any(QuestionSubscribeMember.class));
    }

    @Test
    @DisplayName("라이브 질문 구독자 리스트에 있는경우 추가하지 않는다.")
    void addSubscribeMemberWhenDuplicated() throws ExecutionException, InterruptedException {
        // given
        Member member1 = Member.builder()
            .id(1L)
            .nickname("member1")
            .email("member1")
            .sub("member1")
            .role(ROLE_ACADEMIER)
            .build();
        Question question = Question.builder()
            .id(1L)
            .content("question1")
            .questionStatus(LIVE)
            .build();
        Answer answer = Answer.builder()
            .id(1L)
            .question(question)
            .member(member1)
            .content("answer")
            .build();
        QuestionSubscribeMember questionSubscribeMember1 = QuestionSubscribeMember.builder()
            .id(1L)
            .question(question)
            .member(member1)
            .build();

        doReturn(List.of(questionSubscribeMember1)).when(questionSubscribeMemberRepository).getQuestionSubscribeMemberByQuestion(any());
        doNothing().when(notificationService).sendLiveAnswerAddedNotification(any(),any(),any());

        // When
        CompletableFuture<Void> voidCompletableFuture = questionSubscribeMemberService.addMemberAndSendLiveAnswerAddedNotification(member1, question, answer);

        // Then
        voidCompletableFuture.get();
        verify(questionSubscribeMemberRepository, times(0)).save(any(QuestionSubscribeMember.class));
    }

    @Test
    @DisplayName("질문 구독자 리스트에 있는 actor인 경우 true를 반환한다.")
    void sendLiveAnswerAddedNotification_returnTrueDuplicatedActor() {
        // given
        Member member1 = Member.builder()
            .id(1L)
            .nickname("member1")
            .email("member1")
            .sub("member1")
            .role(ROLE_ACADEMIER)
            .build();
        Question question = Question.builder()
            .id(1L)
            .content("question1")
            .questionStatus(LIVE)
            .build();
        Answer answer = Answer.builder()
            .id(1L)
            .question(question)
            .member(member1)
            .content("answer")
            .build();
        QuestionSubscribeMember questionSubscribeMember1 = QuestionSubscribeMember.builder()
            .id(1L)
            .question(question)
            .member(member1)
            .build();

        doReturn(List.of(questionSubscribeMember1)).when(questionSubscribeMemberRepository).getQuestionSubscribeMemberByQuestion(any());
        doNothing().when(notificationService).sendLiveAnswerAddedNotification(any(),any(),any());

        // when
        boolean result = questionSubscribeMemberService.sendLiveAnswerAddedNotification(member1, question, answer);

        // then
        assertThat(result).isTrue();
    }

    @Test
    @DisplayName("질문 구독자 리스트에 있는 actor인 경우 true를 반환한다.")
    void sendLiveAnswerAddedNotification_returnFalseNewActor() {
        // given
        Member member1 = Member.builder()
            .id(1L)
            .nickname("member1")
            .email("member1")
            .sub("member1")
            .role(ROLE_ACADEMIER)
            .build();
        Question question = Question.builder()
            .id(1L)
            .content("question1")
            .questionStatus(LIVE)
            .build();
        Answer answer = Answer.builder()
            .id(1L)
            .question(question)
            .member(member1)
            .content("answer")
            .build();

        doReturn(List.of()).when(questionSubscribeMemberRepository).getQuestionSubscribeMemberByQuestion(any());
        doNothing().when(notificationService).sendLiveAnswerAddedNotification(any(),any(),any());

        // when
        boolean result = questionSubscribeMemberService.sendLiveAnswerAddedNotification(member1, question, answer);

        // then
        assertThat(result).isFalse();
    }
}