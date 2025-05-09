package com.server.capple.domain.answer.service;

import com.server.capple.domain.answer.dto.AnswerRequest;
import com.server.capple.domain.answer.entity.Answer;
import com.server.capple.domain.answerComment.dto.AnswerCommentRequest;
import com.server.capple.domain.answerComment.entity.AnswerComment;
import com.server.capple.domain.member.entity.Member;
import com.server.capple.domain.question.entity.Question;
import com.server.capple.global.exception.RestApiException;
import com.server.capple.support.ConcurrentTestsConfig;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.IntStream;

import static com.server.capple.domain.member.entity.Role.ROLE_ACADEMIER;
import static com.server.capple.domain.question.entity.QuestionStatus.LIVE;
import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Answer 동시성 테스트 ")
public class AnswerConcurrentServiceTest extends ConcurrentTestsConfig {
    private final int numberOfThreads = 20;

    private Member writer;
    private Question question;
    private Answer answer;
    private final int initialHeartCount = 1000;
    private final int initialCommentCount = 1000;

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
            .commentCount(initialCommentCount)
            .heartCount(initialHeartCount)
            .build();
        answerRepository.save(answer);
    }

    @Autowired
    private JdbcTemplate jdbcTemplate;
    @Autowired
    private EntityManager entityManager;

    @AfterEach
    void tearDown() {
        jdbcTemplate.execute("DELETE FROM answer_comment");
        jdbcTemplate.execute("DELETE FROM answer_heart");
        answerRepository.deleteAllInBatch();
        questionRepository.deleteAllInBatch();
        memberRepository.deleteAllInBatch();
        entityManager.clear();
    }

    @Test
    @DisplayName("서비스 클래스 메서드 내에서 좋아요 개수 수정 테스트")
    void answerToggleAnswerHeartTest() {
        // given
        // when
        answerService.toggleAnswerHeart(writer, answer.getId());

        // then
        answer = answerRepository.findById(answer.getId()).get();
        assertThat(answer.getHeartCount()).isEqualTo(initialHeartCount + 1);
    }

    @Test
    @DisplayName("서비스 클래스 메서드 내에서 좋아요 개수 수정 동시성 테스트")
    void answerToggleAnswerHeartConcurrentTest() throws InterruptedException {
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
                    answerService.toggleAnswerHeart(members.get(finalI), answer.getId());
                } catch (RestApiException e) {
                    increaseHeartFailedCnt.incrementAndGet();
                } finally {
                    latch.countDown();
                }
            });
        }
        latch.await();

        // then
        answer = answerRepository.findById(answer.getId()).get();
        assertThat(answer.getHeartCount()).isEqualTo(initialHeartCount + (likeCount - increaseHeartFailedCnt.get()));
    }

    @Test
    @DisplayName("좋아요 개수 수정 테스트")
    void setHeartCount() {
        // given
        // when
        answerConcurrentService.setHeartCount(answer, true);

        // then
        answer = answerRepository.findById(answer.getId()).get();
        assertThat(answer.getHeartCount()).isEqualTo(initialHeartCount + 1);
    }

    @Test
    @DisplayName("좋아요 개수 수정 동시성 테스트")
    void setHeartCountWithConcurrent() throws InterruptedException {
        // given
        int likeCount = 50;

        int countDown = likeCount;
        ExecutorService executorService = Executors.newFixedThreadPool(numberOfThreads);
        CountDownLatch latch = new CountDownLatch(countDown);

        AtomicInteger increaseHeartFailedCnt = new AtomicInteger(0);

        // when
        for (int i = 0; i < likeCount; i++) {
            executorService.submit(() -> {
                try {
                    if (!answerConcurrentService.setHeartCount(answer, true))
                        increaseHeartFailedCnt.incrementAndGet();
                } finally {
                    latch.countDown();
                }
            });
        }
        latch.await();

        // then
        answer = answerRepository.findById(answer.getId()).get();
        assertThat(answer.getHeartCount()).isEqualTo(initialHeartCount + (likeCount - increaseHeartFailedCnt.get()));
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
                    if (!answerConcurrentService.setHeartCount(answer, true))
                        increaseHeartFailedCnt.incrementAndGet();
                } finally {
                    latch.countDown();
                }
            });
        }
        for (int i = 0; i < hateCount; i++) {
            executorService.submit(() -> {
                try {
                    if (!answerConcurrentService.setHeartCount(answer, false))
                        decreaseHeartFailedCnt.incrementAndGet();
                } finally {
                    latch.countDown();
                }
            });
        }
        latch.await();

        // then
        answer = answerRepository.findById(answer.getId()).get();
        assertThat(answer.getHeartCount())
            .isEqualTo(initialHeartCount
                       + (likeCount - increaseHeartFailedCnt.get())
                       - (hateCount - decreaseHeartFailedCnt.get())
            );
    }

    @Test
    @DisplayName("서비스 클래스 메서드 내에서 댓글 개수 증가 테스트")
    void createAnswerCommentTest() {
        // given
        // when
        answerCommentService.createAnswerComment(
            writer,
            answer.getId(),
            AnswerCommentRequest.builder().answerComment("answerComment").build()
        );

        // then
        answer = answerRepository.findById(answer.getId()).get();
        assertThat(answer.getCommentCount()).isEqualTo(initialCommentCount + 1);
    }

    @Test
    @DisplayName("서비스 클래스 메서드 내에서 댓글 개수 증가 동시성 테스트")
    void createAnswerCommentConcurrentTest() throws InterruptedException {
        // given
        int createCount = 50;

        List<Integer> memberIds = IntStream.rangeClosed(1, createCount)
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

        int countDown = createCount;
        ExecutorService executorService = Executors.newFixedThreadPool(numberOfThreads);
        CountDownLatch latch = new CountDownLatch(countDown);

        AtomicInteger increaseCommentFailedCnt = new AtomicInteger(0);

        // when
        for (int i = 0; i < countDown; i++) {
            int finalI = i;
            executorService.submit(() -> {
                try {
                    answerCommentService.createAnswerComment(writer, answer.getId(), AnswerCommentRequest.builder().answerComment(Integer.toString(finalI)).build());
                } catch (RestApiException e) {
                    increaseCommentFailedCnt.incrementAndGet();
                } finally {
                    latch.countDown();
                }
            });
        }
        latch.await();

        // then
        answer = answerRepository.findById(answer.getId()).get();
        assertThat(answer.getCommentCount()).isEqualTo(initialCommentCount + (createCount - increaseCommentFailedCnt.get()));
    }

    @Test
    @DisplayName("서비스 클래스 메서드 내에서 댓글 개수 감소 테스트")
    void deleteAnswerCommentTest() {
        // given
        AnswerComment answerComment = AnswerComment.builder()
            .member(writer)
            .answer(answer)
            .content("answerComment")
            .heartCount(0)
            .build();
        answerCommentRepository.save(answerComment);

        // when
        answerCommentService.deleteAnswerComment(writer, answerComment.getId());

        // then
        answer = answerRepository.findById(answer.getId()).get();
        assertThat(answer.getCommentCount()).isEqualTo(initialCommentCount - 1);
    }

    @Test
    @DisplayName("서비스 클래스 메서드 내에서 댓글 개수 감소 동시성 테스트")
    void deleteAnswerCommentConcurrentTest() throws InterruptedException {
        // given
        int deleteCount = 50;

        List<Integer> memberIds = IntStream.rangeClosed(1, deleteCount)
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
        List<AnswerComment> answerComments = members.stream().map(member ->
            AnswerComment.builder()
                .member(member)
                .answer(answer)
                .content(member.getNickname())
                .heartCount(0)
                .build()
        ).toList();
        answerCommentRepository.saveAll(answerComments);

        int countDown = deleteCount;
        ExecutorService executorService = Executors.newFixedThreadPool(numberOfThreads);
        CountDownLatch latch = new CountDownLatch(countDown);

        AtomicInteger decreaseCommentFailedCnt = new AtomicInteger(0);

        // when
        for (int i = 0; i < deleteCount; i++) {
            int finalI = i;
            executorService.submit(() -> {
                try {
                    answerCommentService.deleteAnswerComment(writer, answerComments.get(finalI).getId());
                } catch (RestApiException e) {
                    decreaseCommentFailedCnt.incrementAndGet();
                } finally {
                    latch.countDown();
                }
            });
        }
        latch.await();

        // then
        answer = answerRepository.findById(answer.getId()).get();
        assertThat(answer.getCommentCount()).isEqualTo(initialCommentCount - (deleteCount - decreaseCommentFailedCnt.get()));
    }

    @Test
    @DisplayName("댓글 개수 증가 테스트")
    void increaseCommentCountTest() {
        // given
        // when
        answerConcurrentService.increaseCommentCount(answer);

        // then
        answer = answerRepository.findById(answer.getId()).get();
        assertThat(answer.getCommentCount()).isEqualTo(initialCommentCount + 1);
    }

    @Test
    @DisplayName("댓글 개수 증가 동시성 테스트")
    void increaseCommentCountConcurrentTest() throws InterruptedException {
        // given
        int createCount = 50;

        int countDown = createCount;
        ExecutorService executorService = Executors.newFixedThreadPool(numberOfThreads);
        CountDownLatch latch = new CountDownLatch(countDown);

        AtomicInteger increaseCommentFailedCnt = new AtomicInteger(0);

        // when
        for (int i = 0; i < createCount; i++) {
            executorService.submit(() -> {
                try {
                    if (!answerConcurrentService.increaseCommentCount(answer))
                        increaseCommentFailedCnt.incrementAndGet();
                } finally {
                    latch.countDown();
                }
            });
        }
        latch.await();

        // then
        answer = answerRepository.findById(answer.getId()).get();
        assertThat(answer.getCommentCount()).isEqualTo(initialCommentCount + (createCount - increaseCommentFailedCnt.get()));
    }

    @Test
    @DisplayName("댓글 개수 감소 테스트")
    void decreaseCommentCountTest() {
        // given
        // when
        answerConcurrentService.decreaseCommentCount(answer);

        // then
        answer = answerRepository.findById(answer.getId()).get();
        assertThat(answer.getCommentCount()).isEqualTo(initialCommentCount - 1);
    }

    @Test
    @DisplayName("댓글 개수 감소 동시성 테스트")
    void decreaseCommentCountConcurrentTest() throws InterruptedException {
        // given
        int deleteCount = 50;

        int countDown = deleteCount;
        ExecutorService executorService = Executors.newFixedThreadPool(numberOfThreads);
        CountDownLatch latch = new CountDownLatch(countDown);

        AtomicInteger decreaseCommentFailedCnt = new AtomicInteger(0);

        // when
        for (int i = 0; i < deleteCount; i++) {
            executorService.submit(() -> {
                try {
                    if (!answerConcurrentService.decreaseCommentCount(answer))
                        decreaseCommentFailedCnt.incrementAndGet();
                } finally {
                    latch.countDown();
                }
            });
        }
        latch.await();

        // then
        answer = answerRepository.findById(answer.getId()).get();
        assertThat(answer.getCommentCount()).isEqualTo(initialCommentCount - (deleteCount - decreaseCommentFailedCnt.get()));
    }

    @Test
    @DisplayName("좋아요 개수, 댓글 개수, 컨텐츠 수정 동시성 통합 테스트")
    void answerUpdateConcurrentTest() throws InterruptedException {
        // given
        int updateBoardContent = 100; // lock을 사용하지 않는 메서드 사용
        int likeCount = 50;
        int createCount = 50;
        int hateCount = 50;
        int deleteCount = 50;

        int countDown = likeCount + hateCount + updateBoardContent + createCount + deleteCount;
        ExecutorService executorService = Executors.newFixedThreadPool(numberOfThreads);
        CountDownLatch latch = new CountDownLatch(countDown);

        AtomicInteger increaseHeartFailedCnt = new AtomicInteger(0);
        AtomicInteger decreaseHeartFailedCnt = new AtomicInteger(0);
        AtomicInteger increaseCommentFailedCnt = new AtomicInteger(0);
        AtomicInteger decreaseCommentFailedCnt = new AtomicInteger(0);

        // when
        for (int n = 0; n < 10; n++) {
            for (int i = 0; i < updateBoardContent / 10; i++) {
                int finalI = i;
                executorService.submit(() -> {
                    try {
                        answerService.updateAnswer(writer, answer.getId(), AnswerRequest.builder().answer(Integer.toString(finalI)).build());
                    } finally {
                        latch.countDown();
                    }
                });
            }
            for (int i = 0; i < likeCount / 10; i++) {
                executorService.submit(() -> {
                    try {
                        if (!answerConcurrentService.setHeartCount(answer, true))
                            increaseHeartFailedCnt.incrementAndGet();
                    } finally {
                        latch.countDown();
                    }
                });
            }
            for (int i = 0; i < createCount / 10; i++) {
                executorService.submit(() -> {
                    try {
                        if (!answerConcurrentService.increaseCommentCount(answer))
                            increaseCommentFailedCnt.incrementAndGet();
                    } finally {
                        latch.countDown();
                    }
                });
            }
            for (int i = 0; i < hateCount / 10; i++) {
                executorService.submit(() -> {
                    try {
                        if (!answerConcurrentService.setHeartCount(answer, false))
                            decreaseHeartFailedCnt.incrementAndGet();
                    } finally {
                        latch.countDown();
                    }
                });
            }
            for (int i = 0; i < deleteCount / 10; i++) {
                executorService.submit(() -> {
                    try {
                        if (!answerConcurrentService.decreaseCommentCount(answer))
                            decreaseCommentFailedCnt.incrementAndGet();
                    } finally {
                        latch.countDown();
                    }
                });
            }
        }
        latch.await();

        // then
        answer = answerRepository.findById(answer.getId()).get();
        assertThat(answer.getHeartCount())
            .isEqualTo(initialHeartCount
                       + (likeCount - increaseHeartFailedCnt.get())
                       - (hateCount - decreaseHeartFailedCnt.get())
            );
        assertThat(answer.getCommentCount())
            .isEqualTo(initialCommentCount
                       + (createCount - increaseCommentFailedCnt.get())
                       - (deleteCount - decreaseCommentFailedCnt.get())
            );
    }
}
