package com.server.capple.domain.board.service;

import com.server.capple.domain.board.entity.Board;
import com.server.capple.domain.member.entity.Member;
import com.server.capple.support.ConcurrentTestsConfig;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

import static com.server.capple.domain.board.entity.BoardType.FREEBOARD;
import static com.server.capple.domain.member.entity.Role.ROLE_ACADEMIER;
import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Board 동시성 테스트 ")
class BoardConcurrentServiceTest extends ConcurrentTestsConfig {
    private final int numberOfThreads = 20;

    @AfterEach
    void tearDown() {
        jdbcTemplate.execute("DELETE FROM board_comment");
        boardRepository.deleteAllInBatch();
        memberRepository.deleteAllInBatch();
        entityManager.clear();
    }

    @Test
    @DisplayName("좋아요 개수 수정 테스트")
    void setHeartCount() {
        // given
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

        // when
        boardConcurrentService.setHeartCount(board, true);

        // then
        board = boardRepository.findById(board.getId()).get();
        assertThat(board.getHeartCount()).isEqualTo(1);
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
            .heartCount(initialHeartCount)
            .isReport(false)
            .build();
        boardRepository.save(board);
        final Board finalBoard = board;

        int countDown = likeCount;
        ExecutorService executorService = Executors.newFixedThreadPool(numberOfThreads);
        CountDownLatch latch = new CountDownLatch(countDown);

        AtomicInteger increaseHeartFailedCnt = new AtomicInteger(0);

        // when
        for (int i = 0; i < likeCount; i++) {
            executorService.submit(() -> {
                try {
                    if (!boardConcurrentService.setHeartCount(finalBoard, true))
                        increaseHeartFailedCnt.incrementAndGet();
                } finally {
                    latch.countDown();
                }
            });
        }
        latch.await();

        // then
        board = boardRepository.findById(board.getId()).get();
        assertThat(board.getHeartCount()).isEqualTo(initialHeartCount + (likeCount - increaseHeartFailedCnt.get()));
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
        final Board finalBoard = board;

        int countDown = likeCount + hateCount;
        ExecutorService executorService = Executors.newFixedThreadPool(numberOfThreads);
        CountDownLatch latch = new CountDownLatch(countDown);

        AtomicInteger increaseHeartFailedCnt = new AtomicInteger(0);
        AtomicInteger decreaseHeartFailedCnt = new AtomicInteger(0);

        // when
        for (int i = 0; i < likeCount; i++) {
            executorService.submit(() -> {
                try {
                    if (!boardConcurrentService.setHeartCount(finalBoard, true))
                        increaseHeartFailedCnt.incrementAndGet();
                } finally {
                    latch.countDown();
                }
            });
        }
        for (int i = 0; i < hateCount; i++) {
            executorService.submit(() -> {
                try {
                    if (!boardConcurrentService.setHeartCount(finalBoard, false))
                        decreaseHeartFailedCnt.incrementAndGet();
                } finally {
                    latch.countDown();
                }
            });
        }
        latch.await();

        // then
        board = boardRepository.findById(board.getId()).get();
        assertThat(board.getHeartCount())
            .isEqualTo(initialHeartCount
                       + (likeCount - increaseHeartFailedCnt.get())
                       - (hateCount - decreaseHeartFailedCnt.get())
            );
    }

    @Test
    @DisplayName("댓글 개수 증가 테스트")
    void increaseCommentCount() {
        // given
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
        board = boardRepository.findById(board.getId()).get();

        // when
        boardConcurrentService.increaseCommentCount(board);

        // then
        board = boardRepository.findById(board.getId()).get();
        assertThat(board.getCommentCount()).isEqualTo(1);
    }

    @Test
    @DisplayName("댓글 개수 증가 동시성 테스트")
    void increaseCommentCountWIthConcurrent() throws InterruptedException {
        // given
        int initialCommentCount = 1000;
        int increaseCommentCount = 50;
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
            .commentCount(initialCommentCount)
            .heartCount(0)
            .isReport(false)
            .build();
        boardRepository.save(board);
        final Board finalBoard = board;

        int countDown = increaseCommentCount;
        ExecutorService executorService = Executors.newFixedThreadPool(numberOfThreads);
        CountDownLatch latch = new CountDownLatch(countDown);

        AtomicInteger increaseCommentFailedCnt = new AtomicInteger(0);

        // when
        for (int i = 0; i < increaseCommentCount; i++) {
            executorService.submit(() -> {
                try {
                    if (!boardConcurrentService.increaseCommentCount(finalBoard))
                        increaseCommentFailedCnt.incrementAndGet();
                } finally {
                    latch.countDown();
                }
            });
        }
        latch.await();

        // then
        board = boardRepository.findById(board.getId()).get();
        assertThat(board.getCommentCount())
            .isEqualTo(initialCommentCount +
                       (increaseCommentCount - increaseCommentFailedCnt.get()));
    }

    @Test
    @DisplayName("댓글 개수 감소 테스트")
    void decreaseCommentCount() {
        // given
        int initialCommentCount = 1000;
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
            .commentCount(initialCommentCount)
            .heartCount(0)
            .isReport(false)
            .build();
        boardRepository.save(board);
        board = boardRepository.findById(board.getId()).get();

        // when
        boardConcurrentService.decreaseCommentCount(board);

        // then
        board = boardRepository.findById(board.getId()).get();
        assertThat(board.getCommentCount()).isEqualTo(999);
    }

    @Test
    @DisplayName("댓글 개수 감소 동시성 테스트")
    void decreaseCommentCountWIthConcurrent() throws InterruptedException {
        // given
        int initialCommentCount = 1000;
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
            .commentCount(initialCommentCount)
            .heartCount(0)
            .isReport(false)
            .build();
        boardRepository.save(board);
        final Board finalBoard = board;

        int countDown = hateCount;
        ExecutorService executorService = Executors.newFixedThreadPool(numberOfThreads);
        CountDownLatch latch = new CountDownLatch(countDown);

        AtomicInteger decreaseCommentFailedCnt = new AtomicInteger(0);

        // when
        for (int i = 0; i < hateCount; i++) {
            executorService.submit(() -> {
                try {
                    if (!boardConcurrentService.decreaseCommentCount(finalBoard))
                        decreaseCommentFailedCnt.incrementAndGet();
                } finally {
                    latch.countDown();
                }
            });
        }
        latch.await();

        // then
        board = boardRepository.findById(board.getId()).get();
        assertThat(board.getCommentCount()).isEqualTo(initialCommentCount - (hateCount - decreaseCommentFailedCnt.get()));
    }

    @Test
    @DisplayName("좋아요 개수, 댓글 개수, 컨텐츠 수정 동시성 통합 테스트")
    void boardUpdateConcurrentTest() throws InterruptedException {
        // given
        int initialHeartCount = 1000;
        int initialCommentCount = 1000;
        int updateBoardContent = 50; // lock을 사용하지 않는 메서드 사용
        int likeCount = 20;
        int increaseCommentCount = 20;
        int hateCount = 20;
        int decreaseCommentCount = 20;
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
            .heartCount(initialHeartCount)
            .commentCount(initialCommentCount)
            .isReport(false)
            .build();
        boardRepository.save(board);
        final Board finalBoard = board;

        int countDown = likeCount + hateCount + updateBoardContent + increaseCommentCount + decreaseCommentCount;
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
                        boardService.updateBoard(writer, finalBoard.getId(), Integer.toString(finalI));
                    } finally {
                        latch.countDown();
                    }
                });
            }
            for (int i = 0; i < likeCount / 10; i++) {
                executorService.submit(() -> {
                    try {
                        if (!boardConcurrentService.setHeartCount(finalBoard, true))
                            increaseHeartFailedCnt.incrementAndGet();
                    } finally {
                        latch.countDown();
                    }
                });
            }
            for (int i = 0; i < increaseCommentCount / 10; i++) {
                executorService.submit(() -> {
                    try {
                        if (!boardConcurrentService.increaseCommentCount(finalBoard))
                            increaseCommentFailedCnt.incrementAndGet();
                    } finally {
                        latch.countDown();
                    }
                });
            }
            for (int i = 0; i < hateCount / 10; i++) {
                executorService.submit(() -> {
                    try {
                        if (!boardConcurrentService.setHeartCount(finalBoard, false))
                            decreaseHeartFailedCnt.incrementAndGet();
                    } finally {
                        latch.countDown();
                    }
                });
            }
            for (int i = 0; i < decreaseCommentCount / 10; i++) {
                executorService.submit(() -> {
                    try {
                        if (!boardConcurrentService.decreaseCommentCount(finalBoard))
                            decreaseCommentFailedCnt.incrementAndGet();
                    } finally {
                        latch.countDown();
                    }
                });
            }
        }
        latch.await();

        // then
        board = boardRepository.findById(board.getId()).get();
        assertThat(board.getHeartCount())
            .isEqualTo(initialHeartCount
                       + (likeCount - increaseHeartFailedCnt.get())
                       - (hateCount - decreaseHeartFailedCnt.get())
            );
        assertThat(board.getCommentCount())
            .isEqualTo(initialCommentCount
                       + (increaseCommentCount - increaseCommentFailedCnt.get())
                       - (decreaseCommentCount - decreaseCommentFailedCnt.get())
            );
    }
}