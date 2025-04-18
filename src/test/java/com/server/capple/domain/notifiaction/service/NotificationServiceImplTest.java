package com.server.capple.domain.notifiaction.service;

import com.server.capple.config.apns.service.ApnsService;
import com.server.capple.domain.answer.entity.Answer;
import com.server.capple.domain.answer.repository.AnswerRepository;
import com.server.capple.domain.member.entity.Member;
import com.server.capple.domain.member.repository.MemberRepository;
import com.server.capple.domain.notifiaction.dto.NotificationDBResponse.NotificationDBInfo;
import com.server.capple.domain.notifiaction.repository.NotificationRepository;
import com.server.capple.domain.question.entity.Question;
import com.server.capple.domain.question.repository.QuestionRepository;
import org.awaitility.Awaitility;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.ActiveProfiles;

import java.time.Duration;

import static com.server.capple.domain.member.entity.Role.ROLE_ACADEMIER;
import static com.server.capple.domain.notifiaction.entity.NotificationType.ANSWER_HEART;
import static com.server.capple.domain.question.entity.QuestionStatus.LIVE;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ActiveProfiles("test")
@SpringBootTest
@DisplayName("NotificationService 로 ")
class NotificationServiceImplTest {
    @Autowired
    private NotificationServiceImpl notificationServiceImpl;
    @MockBean
    private ApnsService apnsService;
    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    private QuestionRepository questionRepository;
    @Autowired
    private AnswerRepository answerRepository;
    @Autowired
    private NotificationRepository notificationRepository;

    @AfterEach
    void tearDown() {
        notificationRepository.deleteAll();
        answerRepository.deleteAllInBatch();
        questionRepository.deleteAllInBatch();
        memberRepository.deleteAllInBatch();
    }

    @Test
    @DisplayName("사용자의 답변이 아닌 답변에 좋아요를 누를 경우 알림을 보낸다.")
    void sendAnswerHeartNotification() {
        // given
        Member actor = Member.builder()
            .nickname("actor")
            .email("actor")
            .sub("actor")
            .role(ROLE_ACADEMIER)
            .build();
        Member savedActor = memberRepository.save(actor);
        Member author = Member.builder()
            .nickname("author")
            .email("author")
            .sub("author")
            .role(ROLE_ACADEMIER)
            .build();
        Member savedAuthor = memberRepository.save(author);
        Question question = Question.builder()
            .content("question")
            .questionStatus(LIVE)
            .build();
        Question saveQuestion = questionRepository.save(question);
        Answer answer = Answer.builder()
            .content("answer")
            .question(saveQuestion)
            .member(savedAuthor)
            .build();
        Answer saveAnswer = answerRepository.save(answer);

        Mockito.doReturn(true).when(apnsService).sendApnsToMembers(any(), anyLong());

        // when
        notificationServiceImpl.sendAnswerHeartNotification(savedActor.getId(), saveAnswer);

        // then
        Awaitility.await()
            .atMost(Duration.ofSeconds(2))
            .untilAsserted(() -> {
                PageRequest pageRequest = PageRequest.of(0, 10, Sort.by(Sort.Direction.DESC, "createdAt"));
                Slice<NotificationDBInfo> infoSlice = notificationRepository.findByMemberId(author, null, pageRequest);
                verify(apnsService, times(1)).sendApnsToMembers(any(), anyLong());
                assertThat(infoSlice.getContent()).hasSize(1)
                    .extracting("isResponsedQuestion", "notification.type.title", "notification.notificationLog.answer.content")
                    .containsExactlyInAnyOrder(tuple(true, ANSWER_HEART.getTitle(), saveAnswer.getContent()));
            });
    }

    @Test
    @DisplayName("자신의 답변에 좋아요를 누를 경우 알림을 보내지 않는다.")
    void sendAnswerHeartNotification2() {
        // given
        Member author = Member.builder()
            .nickname("author")
            .email("author")
            .sub("author")
            .role(ROLE_ACADEMIER)
            .build();
        Member savedAuthor = memberRepository.save(author);
        Question question = Question.builder()
            .content("question")
            .questionStatus(LIVE)
            .build();
        Question saveQuestion = questionRepository.save(question);
        Answer answer = Answer.builder()
            .content("answer")
            .question(saveQuestion)
            .member(savedAuthor)
            .build();
        Answer saveAnswer = answerRepository.save(answer);

        Mockito.doReturn(true).when(apnsService).sendApnsToMembers(any(), anyLong());

        // when
        notificationServiceImpl.sendAnswerHeartNotification(savedAuthor.getId(), saveAnswer);

        // then
        Awaitility.await()
            .atMost(Duration.ofSeconds(2))
            .untilAsserted(() -> {
                PageRequest pageRequest = PageRequest.of(0, 10, Sort.by(Sort.Direction.DESC, "createdAt"));
                Slice<NotificationDBInfo> infoSlice = notificationRepository.findByMemberId(author, null, pageRequest);
                verify(apnsService, times(0)).sendApnsToMembers(any(), anyLong());
                assertThat(infoSlice.getContent()).hasSize(0);
            });
    }
}