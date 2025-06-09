package com.server.capple.domain.answerComment.service;

import com.server.capple.domain.answer.entity.Answer;
import com.server.capple.domain.answerComment.dto.AnswerCommentRequest;
import com.server.capple.domain.answerComment.entity.AnswerComment;
import com.server.capple.domain.member.entity.Member;
import com.server.capple.domain.question.entity.Question;
import com.server.capple.global.exception.RestApiException;
import com.server.capple.support.ConcurrentTestsConfig;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.IntStream;

import static com.server.capple.domain.member.entity.Role.ROLE_ACADEMIER;
import static com.server.capple.domain.question.entity.QuestionStatus.LIVE;
import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("AnswerComment 동시성 테스트 ")
public class AnswerCommentConcurrentServiceTest extends ConcurrentTestsConfig {
    private final int numberOfThreads = 20;

    private Member writer;
    private Question question;
    private Answer answer;
    private AnswerComment answerComment;
    private final int initialHeartCount = 1000;

    @BeforeEach
    void setUp() {
        writer = Member.builder()
            .nickname("writer")
            .email("email")
            .sub("sub")
            .role(ROLE_ACADEMIER)
            .build();
        memberRepository.save(writer);
        question = Question.builder()
            .content("question")
            .questionStatus(LIVE)
            .build();
        questionRepository.save(question);
        answer = Answer.builder()
            .member(writer)
            .question(question)
            .content("answer")
            .commentCount(0)
            .heartCount(0)
            .build();
        answerRepository.save(answer);
        answerComment = AnswerComment.builder()
            .member(writer)
            .answer(answer)
            .content("answerContent")
            .heartCount(initialHeartCount)
            .build();
        answerCommentRepository.save(answerComment);
    }

    @AfterEach
    void tearDown() {
        answerCommentHeartRepository.deleteAllInBatch();
        answerCommentRepository.deleteAllInBatch();
        answerRepository.deleteAllInBatch();
        questionRepository.deleteAllInBatch();
        memberRepository.deleteAllInBatch();
        entityManager.clear();
        redisTemplate.delete(redisTemplate.keys("*"));
    }

    @Test
    @DisplayName("서비스 클래스 메서드 내에서 좋아요 변경 테스트")
    void answerCommentSetHeartCountTest() {
        // given
        // when
        answerCommentService.toggleAnswerCommentHeart(writer, answerComment.getId());

        // then
        answerComment = answerCommentRepository.findById(answerComment.getId()).get();
        assertThat(answerComment.getHeartCount()).isEqualTo(initialHeartCount + 1);
    }

    @Test
    @DisplayName("서비스 클래스 메서드 내에서 동시성 테스트")
    void answerCommentSetHeartCountConcurrentTest() throws InterruptedException {
        // given
        int likeCount = 50;

        List<Integer> memberIds = IntStream.rangeClosed(1, likeCount)
            .boxed()
            .toList();
        List<Member> members = memberIds.stream().map(memberId -> {
            String memberIdString = memberId.toString();
            return Member.builder()
                .nickname(memberIdString)
                .email(memberIdString)
                .sub(memberIdString)
                .role(ROLE_ACADEMIER)
                .build();
        }).toList();
        memberRepository.saveAll(members);

        int countDown = likeCount;
        ExecutorService executorService = Executors.newFixedThreadPool(numberOfThreads);
        CountDownLatch latch = new CountDownLatch(countDown);

        AtomicInteger increaseHeartFailedCnt = new AtomicInteger(0);

        // when
        for (int i = 0; i < likeCount; i++) {
            int finalI = i;
            executorService.submit(() -> {
                try {
                    answerCommentService.toggleAnswerCommentHeart(members.get(finalI), answerComment.getId());
                } catch (RestApiException e) {
                    increaseHeartFailedCnt.incrementAndGet();
                } finally {
                    latch.countDown();
                }
            });
        }
        latch.await();

        // then
        answerComment = answerCommentRepository.findById(answerComment.getId()).get();
        assertThat(answerComment.getHeartCount()).isEqualTo(initialHeartCount + (likeCount - increaseHeartFailedCnt.get()));
    }

    @Test
    @DisplayName("좋아요 개수 수정 테스트")
    void setHeartCount() {
        // given
        // when
        answerCommentConcurrentService.setHeartCount(answerComment, true);

        // then
        answerComment = answerCommentRepository.findById(answerComment.getId()).get();
        assertThat(answerComment.getHeartCount()).isEqualTo(initialHeartCount + 1);
    }

    @Test
    @DisplayName("좋아요 개수 수정 동시성 테스트")
    void setHeartCountWithConcurrent() throws InterruptedException {
        // given
        int initialHeartCount = 1000;
        int likeCount = 50;

        int countDown = likeCount;
        ExecutorService executorService = Executors.newFixedThreadPool(numberOfThreads);
        CountDownLatch latch = new CountDownLatch(countDown);

        AtomicInteger increaseHeartFailedCnt = new AtomicInteger(0);

        // when
        for (int i = 0; i < likeCount; i++) {
            executorService.submit(() -> {
                try {
                    if (!answerCommentConcurrentService.setHeartCount(answerComment, true))
                        increaseHeartFailedCnt.incrementAndGet();
                } finally {
                    latch.countDown();
                }
            });
        }
        latch.await();

        // then
        answerComment = answerCommentRepository.findById(answerComment.getId()).get();
        assertThat(answerComment.getHeartCount()).isEqualTo(initialHeartCount + (likeCount - increaseHeartFailedCnt.get()));
    }

    @Test
    @DisplayName("좋아요 증가,감소 수정 동시성 테스트")
    void setHeartCountWithConcurrentLikeAndHate() throws InterruptedException {
        // given
        int likeCount = 50;
        int hateCount = 50;

        int countDown = likeCount + hateCount;
        ExecutorService executorService = Executors.newFixedThreadPool(numberOfThreads);
        CountDownLatch latch = new CountDownLatch(countDown);

        AtomicInteger increaseHeartFailedCnt = new AtomicInteger(0);
        AtomicInteger decreaseHeartFailedCnt = new AtomicInteger(0);

        // when
        for (int i = 0; i < likeCount; i++) {
            executorService.submit(() -> {
                try {
                    if (!answerCommentConcurrentService.setHeartCount(answerComment, true))
                        increaseHeartFailedCnt.incrementAndGet();
                } finally {
                    latch.countDown();
                }
            });
        }
        for (int i = 0; i < hateCount; i++) {
            executorService.submit(() -> {
                try {
                    if (!answerCommentConcurrentService.setHeartCount(answerComment, false))
                        decreaseHeartFailedCnt.incrementAndGet();
                } finally {
                    latch.countDown();
                }
            });
        }
        latch.await();

        // then
        answerComment = answerCommentRepository.findById(answerComment.getId()).get();
        assertThat(answerComment.getHeartCount())
            .isEqualTo(initialHeartCount
                       + (likeCount - increaseHeartFailedCnt.get())
                       - (hateCount - decreaseHeartFailedCnt.get())
            );
    }

    @Test
    @DisplayName("좋아요 개수, 컨텐츠 수정 동시성 통합 테스트")
    void setHeartCountWithConcurrentLikeAndHateAndContent() throws InterruptedException {
        // given
        int likeCount = 50;
        int hateCount = 50;
        int contentChangeCount = 50;

        int countDown = likeCount + hateCount + contentChangeCount;
        ExecutorService executorService = Executors.newFixedThreadPool(numberOfThreads);
        CountDownLatch latch = new CountDownLatch(countDown);

        AtomicInteger increaseHeartFailedCnt = new AtomicInteger(0);
        AtomicInteger decreaseHeartFailedCnt = new AtomicInteger(0);

        // when
        for (int i = 0; i < likeCount; i++) {
            executorService.submit(() -> {
                try {
                    if (!answerCommentConcurrentService.setHeartCount(answerComment, true))
                        increaseHeartFailedCnt.incrementAndGet();
                } finally {
                    latch.countDown();
                }
            });
        }
        for (int i = 0; i < hateCount; i++) {
            executorService.submit(() -> {
                try {
                    if (!answerCommentConcurrentService.setHeartCount(answerComment, false))
                        decreaseHeartFailedCnt.incrementAndGet();
                } finally {
                    latch.countDown();
                }
            });
        }
        for (int i = 0; i < contentChangeCount; i++) {
            int finalI = i;
            executorService.submit(() -> {
                answerCommentService.updateAnswerComment(writer, answerComment.getId(), AnswerCommentRequest.builder().answerComment(Integer.toString(finalI)).build());
                latch.countDown();
            });
        }
        latch.await();

        // then
        answerComment = answerCommentRepository.findById(answerComment.getId()).get();
        assertThat(answerComment.getHeartCount())
            .isEqualTo(initialHeartCount
                       + (likeCount - increaseHeartFailedCnt.get())
                       - (hateCount - decreaseHeartFailedCnt.get())
            );
    }
}
