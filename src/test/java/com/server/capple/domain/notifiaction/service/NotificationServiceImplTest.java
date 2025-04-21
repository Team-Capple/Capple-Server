package com.server.capple.domain.notifiaction.service;

import com.server.capple.config.apns.service.ApnsService;
import com.server.capple.domain.answer.entity.Answer;
import com.server.capple.domain.answer.repository.AnswerRepository;
import com.server.capple.domain.answerComment.entity.AnswerComment;
import com.server.capple.domain.answerComment.repository.AnswerCommentRepository;
import com.server.capple.domain.answerSubscribeMember.repository.AnswerSubscribeMemberRepository;
import com.server.capple.domain.member.entity.Member;
import com.server.capple.domain.member.repository.MemberRepository;
import com.server.capple.domain.notifiaction.dto.NotificationDBResponse.NotificationDBInfo;
import com.server.capple.domain.notifiaction.repository.NotificationRepository;
import com.server.capple.domain.question.entity.Question;
import com.server.capple.domain.question.repository.QuestionRepository;
import org.awaitility.Awaitility;
import org.junit.jupiter.api.*;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.ActiveProfiles;

import java.time.Duration;
import java.util.Collection;
import java.util.List;

import static com.server.capple.domain.member.entity.Role.ROLE_ACADEMIER;
import static com.server.capple.domain.notifiaction.entity.NotificationType.*;
import static com.server.capple.domain.question.entity.QuestionStatus.LIVE;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;
import static org.mockito.ArgumentMatchers.*;
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
    private AnswerCommentRepository answerCommentRepository;
    @Autowired
    private AnswerSubscribeMemberRepository answerSubscribeMemberRepository;
    @Autowired
    private NotificationRepository notificationRepository;

    @AfterEach
    void tearDown() {
        notificationRepository.deleteAll();
        answerSubscribeMemberRepository.deleteAllInBatch();
        answerCommentRepository.deleteAllInBatch();
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

    @DisplayName("답변의 댓글이 생길경우 ")
    @TestFactory
    Collection<DynamicTest> sendAnswerCommentNotification() {
        // given
        Member answerAuthor = Member.builder()
            .nickname("answerAuthor")
            .email("answerAuthor")
            .sub("answerAuthor")
            .role(ROLE_ACADEMIER)
            .build();
        Member prevCommenter = Member.builder()
            .nickname("prevCommenter")
            .email("prevCommenter")
            .sub("prevCommenter")
            .role(ROLE_ACADEMIER)
            .build();
        Member actor = Member.builder()
            .nickname("actor")
            .email("actor")
            .sub("actor")
            .role(ROLE_ACADEMIER)
            .build();
        memberRepository.saveAll(List.of(answerAuthor, prevCommenter, actor));
        Question question = Question.builder()
            .content("question")
            .questionStatus(LIVE)
            .build();
        questionRepository.save(question);
        Answer answer = Answer.builder()
            .content("answer")
            .question(question)
            .member(answerAuthor)
            .build();
        answerRepository.save(answer);
        AnswerComment answerComment = AnswerComment.builder()
            .content("answerComment")
            .answer(answer)
            .member(prevCommenter)
            .build();
        AnswerComment newAnswerComment = AnswerComment.builder()
            .content("answerComment")
            .answer(answer)
            .member(actor)
            .build();
        answerCommentRepository.saveAll(List.of(answerComment, newAnswerComment));

        Mockito.doReturn(true).when(apnsService).sendApnsToMembers(any(), eq(answerAuthor.getId()));
        Mockito.doReturn(true).when(apnsService).sendApnsToMembers(any(), eq(List.of(prevCommenter.getId())));

        return List.of(
            DynamicTest.dynamicTest("댓글이 없더라도 답변의 작성자에게 알림을 발송한다.", () -> {
                // when
                notificationServiceImpl.sendAnswerCommentNotification(answer, answerComment);

                // then
                Awaitility.await()
                    .atMost(Duration.ofSeconds(4))
                    .untilAsserted(() -> {
                        PageRequest pageRequest = PageRequest.of(0, 10, Sort.by(Sort.Direction.DESC, "createdAt"));
                        Slice<NotificationDBInfo> answerAuthorNotification = notificationRepository.findByMemberId(answerAuthor, null, pageRequest);
                        Slice<NotificationDBInfo> prevCommentAuthorNotification = notificationRepository.findByMemberId(prevCommenter, null, pageRequest);
                        verify(apnsService, times(1)).sendApnsToMembers(any(), eq(answerAuthor.getId()));
                        assertThat(answerAuthorNotification.getContent()).hasSize(1)
                            .extracting("isResponsedQuestion", "notification.type.title", "notification.notificationLog.answer.content", "notification.notificationLog.answerComment.content")
                            .containsExactlyInAnyOrder(
                                tuple(true, ANSWER_COMMENT.getTitle(), answer.getContent(), answerComment.getContent())
                            );
                        assertThat(prevCommentAuthorNotification.getContent()).hasSize(0);
                    });
            }),
            DynamicTest.dynamicTest("답변의 작성자와 기존 댓글 작성자에게 모두 알림을 발송한다.", () -> {
                // when
                notificationServiceImpl.sendAnswerCommentNotification(answer, newAnswerComment);

                // then
                Awaitility.await()
                    .atMost(Duration.ofSeconds(4))
                    .untilAsserted(() -> {
                        PageRequest pageRequest = PageRequest.of(0, 10, Sort.by(Sort.Direction.DESC, "createdAt"));
                        Slice<NotificationDBInfo> answerAuthorNotification = notificationRepository.findByMemberId(answerAuthor, null, pageRequest);
                        Slice<NotificationDBInfo> prevCommentAuthorNotification = notificationRepository.findByMemberId(prevCommenter, null, pageRequest);
                        verify(apnsService, times(2)).sendApnsToMembers(any(), eq(answerAuthor.getId()));
                        verify(apnsService, times(1)).sendApnsToMembers(any(), eq(List.of(prevCommenter.getId())));
                        assertThat(answerAuthorNotification.getContent()).hasSize(2)
                            .extracting("isResponsedQuestion", "notification.type.title", "notification.notificationLog.answer.content", "notification.notificationLog.answerComment.content")
                            .containsExactlyInAnyOrder(
                                tuple(true, ANSWER_COMMENT.getTitle(), answer.getContent(), newAnswerComment.getContent()),
                                tuple(true, ANSWER_COMMENT.getTitle(), answer.getContent(), answerComment.getContent())
                            );
                        assertThat(prevCommentAuthorNotification.getContent()).hasSize(1)
                            .extracting("isResponsedQuestion", "notification.type.title", "notification.notificationLog.answer.content", "notification.notificationLog.answerComment.content")
                            .containsExactlyInAnyOrder(tuple(false, ANSWER_COMMENT_DUPLICATE.getTitle(), answer.getContent(), newAnswerComment.getContent()));
                    });
            })
        );
    }

    @Test
    @DisplayName("답변 댓글의 좋아요 알림을 발송할 수 있다.")
    void sendAnswerCommentHeartNotification() {
        // given
        Member answerAuthor = Member.builder()
            .nickname("answerAuthor")
            .email("answerAuthor")
            .sub("answerAuthor")
            .role(ROLE_ACADEMIER)
            .build();
        Member commentAuthor = Member.builder()
            .nickname("commentAuthor")
            .email("commentAuthor")
            .sub("commentAuthor")
            .role(ROLE_ACADEMIER)
            .build();
        memberRepository.saveAll(List.of(answerAuthor, commentAuthor));
        Question question = Question.builder()
            .content("question")
            .questionStatus(LIVE)
            .build();
        questionRepository.save(question);
        Answer answer = Answer.builder()
            .content("answer")
            .question(question)
            .member(answerAuthor)
            .build();
        answerRepository.save(answer);
        AnswerComment answerComment = AnswerComment.builder()
            .content("answerComment")
            .answer(answer)
            .member(commentAuthor)
            .build();
        answerCommentRepository.save(answerComment);

        // when
        notificationServiceImpl.sendAnswerCommentHeartNotification(answerComment);

        // then

        Awaitility.await()
            .atMost(Duration.ofSeconds(2))
            .untilAsserted(() -> {
                verify(apnsService, times(1)).sendApnsToMembers(any(), eq(commentAuthor.getId()));
                PageRequest pageRequest = PageRequest.of(0, 10, Sort.by(Sort.Direction.DESC, "createdAt"));
                Slice<NotificationDBInfo> notifications = notificationRepository.findByMemberId(commentAuthor, null, pageRequest);
                assertThat(notifications.getContent()).hasSize(1)
                    .extracting("isResponsedQuestion", "notification.type.title", "notification.notificationLog.answer.content", "notification.notificationLog.answerComment.content")
                    .containsExactlyInAnyOrder(
                        tuple(false, ANSWER_COMMENT_HEART.getTitle(), answer.getContent(), answerComment.getContent())
                    );
            });
    }
}