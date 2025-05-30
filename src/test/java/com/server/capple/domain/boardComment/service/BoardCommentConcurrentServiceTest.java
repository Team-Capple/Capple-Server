package com.server.capple.domain.boardComment.service;

import com.server.capple.domain.board.entity.Board;
import com.server.capple.domain.boardComment.entity.BoardComment;
import com.server.capple.domain.member.entity.Member;
import com.server.capple.global.exception.RestApiException;
import com.server.capple.support.ConcurrentTestsConfig;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.IntStream;

import static com.server.capple.domain.board.entity.BoardType.FREEBOARD;
import static com.server.capple.domain.member.entity.Role.ROLE_ACADEMIER;
import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("BoardComment 동시성 테스트 ")
public class BoardCommentConcurrentServiceTest extends ConcurrentTestsConfig {
    private final int numberOfThreads = 20;

    @AfterEach
    void tearDown() {
        boardCommentHeartRepository.deleteAllInBatch();
        boardCommentRepository.deleteAllInBatch();
        boardRepository.deleteAllInBatch();
        memberRepository.deleteAllInBatch();
        entityManager.clear();
    }

    @Test
    @DisplayName("서비스 클래스 메서드 내에서 좋아요 변경 테스트")
    void boardCommentServiceSetHeartCountTest() {
        // given
        int initialHeartCount = 1000;

        Member writer = Member.builder()
            .nickname("writer")
            .email("email")
            .sub("sub")
            .role(ROLE_ACADEMIER)
            .build();
        memberRepository.save(writer);
        Board board = Board.builder()
            .writer(writer)
            .boardType(FREEBOARD)
            .content("content")
            .commentCount(0)
            .heartCount(0)
            .isReport(false)
            .build();
        boardRepository.save(board);
        BoardComment boardComment = BoardComment.builder()
            .board(board)
            .writer(writer)
            .heartCount(initialHeartCount)
            .content("boardComment")
            .isReport(false)
            .build();
        boardCommentRepository.save(boardComment);

        // when
        boardCommentService.toggleBoardCommentHeart(writer, boardComment.getId());

        // then
        boardComment = boardCommentRepository.findById(boardComment.getId()).get();
        assertThat(boardComment.getHeartCount()).isEqualTo(initialHeartCount + 1);
    }

    @Test
    @DisplayName("서비스 클래스 메서드 내에서 동시성 테스트")
    void boardCommentServiceSetHeartCountConcurrentTest() throws InterruptedException {
        // given
        int initialHeartCount = 1000;
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

        Member writer = Member.builder()
            .nickname("writer")
            .email("email")
            .sub("sub")
            .role(ROLE_ACADEMIER)
            .build();
        memberRepository.save(writer);
        Board board = Board.builder()
            .writer(writer)
            .boardType(FREEBOARD)
            .content("content")
            .commentCount(0)
            .heartCount(0)
            .isReport(false)
            .build();
        boardRepository.save(board);
        BoardComment boardComment = BoardComment.builder()
            .board(board)
            .writer(writer)
            .heartCount(initialHeartCount)
            .content("boardComment")
            .isReport(false)
            .build();
        boardCommentRepository.save(boardComment);

        final BoardComment finalBoardComment = boardComment;

        int countDown = likeCount;
        ExecutorService executorService = Executors.newFixedThreadPool(numberOfThreads);
        CountDownLatch latch = new CountDownLatch(countDown);

        AtomicInteger increaseHeartFailedCnt = new AtomicInteger(0);

        // when
        for (int i = 0; i < likeCount; i++) {
            int finalI = i;
            executorService.submit(() -> {
                try {
                    boardCommentService.toggleBoardCommentHeart(members.get(finalI), finalBoardComment.getId());
                } catch (RestApiException e) {
                    increaseHeartFailedCnt.incrementAndGet();
                } finally {
                    latch.countDown();
                }
            });
        }
        latch.await();

        // then
        boardComment = boardCommentRepository.findById(boardComment.getId()).get();
        assertThat(boardComment.getHeartCount()).isEqualTo(initialHeartCount + (likeCount - increaseHeartFailedCnt.get()));
    }

    @Test
    @DisplayName("좋아요 개수 수정 테스트")
    void setHeartCount() {
        // given
        int initialHeartCount = 1000;

        Member writer = Member.builder()
            .nickname("writer")
            .email("email")
            .sub("sub")
            .role(ROLE_ACADEMIER)
            .build();
        memberRepository.save(writer);
        Board board = Board.builder()
            .writer(writer)
            .boardType(FREEBOARD)
            .content("content")
            .commentCount(0)
            .heartCount(0)
            .isReport(false)
            .build();
        boardRepository.save(board);
        BoardComment boardComment = BoardComment.builder()
            .board(board)
            .writer(writer)
            .heartCount(initialHeartCount)
            .content("boardComment")
            .isReport(false)
            .build();
        boardCommentRepository.save(boardComment);

        // when
        boardCommentConcurrentService.setHeartCount(boardComment, true);

        // then
        boardComment = boardCommentRepository.findById(boardComment.getId()).get();
        assertThat(boardComment.getHeartCount()).isEqualTo(initialHeartCount + 1);
    }

    @Test
    @DisplayName("좋아요 개수 수정 동시성 테스트")
    void setHeartCountWIthConcurrent() throws InterruptedException {
        // given
        int initialHeartCount = 1000;
        int likeCount = 50;
        Member writer = Member.builder()
            .nickname("writer")
            .email("email")
            .sub("sub")
            .role(ROLE_ACADEMIER)
            .build();
        memberRepository.save(writer);
        Board board = Board.builder()
            .writer(writer)
            .boardType(FREEBOARD)
            .content("content")
            .commentCount(0)
            .heartCount(0)
            .isReport(false)
            .build();
        boardRepository.save(board);
        BoardComment boardComment = BoardComment.builder()
            .board(board)
            .writer(writer)
            .heartCount(initialHeartCount)
            .content("boardComment")
            .isReport(false)
            .build();
        boardCommentRepository.save(boardComment);

        final BoardComment finalBoardComment = boardComment;

        int countDown = likeCount;
        ExecutorService executorService = Executors.newFixedThreadPool(numberOfThreads);
        CountDownLatch latch = new CountDownLatch(countDown);

        AtomicInteger increaseHeartFailedCnt = new AtomicInteger(0);

        // when
        for (int i = 0; i < likeCount; i++) {
            executorService.submit(() -> {
                try {
                    if (!boardCommentConcurrentService.setHeartCount(finalBoardComment, true))
                        increaseHeartFailedCnt.incrementAndGet();
                } finally {
                    latch.countDown();
                }
            });
        }
        latch.await();

        // then
        boardComment = boardCommentRepository.findById(boardComment.getId()).get();
        assertThat(boardComment.getHeartCount()).isEqualTo(initialHeartCount + (likeCount - increaseHeartFailedCnt.get()));
    }

    @Test
    @DisplayName("좋아요 증가,감소 수정 동시성 테스트")
    void setHeartCountWIthConcurrentLikeAndHate() throws InterruptedException {
        // given
        int initialHeartCount = 1000;
        int likeCount = 50;
        int hateCount = 50;
        Member writer = Member.builder()
            .nickname("writer")
            .email("email")
            .sub("sub")
            .role(ROLE_ACADEMIER)
            .build();
        memberRepository.save(writer);
        Board board = Board.builder()
            .writer(writer)
            .boardType(FREEBOARD)
            .content("content")
            .commentCount(0)
            .heartCount(initialHeartCount)
            .isReport(false)
            .build();
        boardRepository.save(board);
        BoardComment boardComment = BoardComment.builder()
            .board(board)
            .writer(writer)
            .heartCount(initialHeartCount)
            .content("boardComment")
            .isReport(false)
            .build();
        boardCommentRepository.save(boardComment);
        final BoardComment finalBoardComment = boardComment;

        int countDown = likeCount + hateCount;
        ExecutorService executorService = Executors.newFixedThreadPool(numberOfThreads);
        CountDownLatch latch = new CountDownLatch(countDown);

        AtomicInteger increaseHeartFailedCnt = new AtomicInteger(0);
        AtomicInteger decreaseHeartFailedCnt = new AtomicInteger(0);

        // when
        for (int i = 0; i < likeCount; i++) {
            executorService.submit(() -> {
                try {
                    if (!boardCommentConcurrentService.setHeartCount(finalBoardComment, true))
                        increaseHeartFailedCnt.incrementAndGet();
                } finally {
                    latch.countDown();
                }
            });
        }
        for (int i = 0; i < hateCount; i++) {
            executorService.submit(() -> {
                try {
                    if (!boardCommentConcurrentService.setHeartCount(finalBoardComment, false))
                        decreaseHeartFailedCnt.incrementAndGet();
                } finally {
                    latch.countDown();
                }
            });
        }
        latch.await();

        // then
        boardComment = boardCommentRepository.findById(boardComment.getId()).get();
        assertThat(boardComment.getHeartCount())
            .isEqualTo(initialHeartCount
                       + (likeCount - increaseHeartFailedCnt.get())
                       - (hateCount - decreaseHeartFailedCnt.get())
            );
    }
}
