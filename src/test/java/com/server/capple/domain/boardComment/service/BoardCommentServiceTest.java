package com.server.capple.domain.boardComment.service;

import com.server.capple.domain.boardComment.dto.BoardCommentRequest;
import com.server.capple.domain.boardComment.dto.BoardCommentResponse.BoardCommentInfo;
import com.server.capple.domain.boardComment.dto.BoardCommentResponse.ToggleBoardCommentHeart;
import com.server.capple.domain.boardComment.entity.BoardComment;
import com.server.capple.global.common.SliceResponse;
import com.server.capple.support.ServiceTestConfig;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DisplayName("BoardComment 서비스의 ")
@SpringBootTest
public class BoardCommentServiceTest extends ServiceTestConfig {
    @Autowired
    private BoardCommentService boardCommentService;

    @Test
    @DisplayName("게시글 댓글 생성 테스트")
    public void createBoardCommentTest() {
        //given
        BoardCommentRequest request = getBoardCommentRequest();
        int commentCount = board.getCommentCount();
        //when
        Long boardCommentId = boardCommentService.createBoardComment(member, board.getId(), request).getBoardCommentId();
        BoardComment comment = boardCommentService.findBoardComment(boardCommentId);

        //then
        board = boardRepository.findById(board.getId()).get();
        assertEquals("게시글 댓글", comment.getContent());
        assertEquals(commentCount + 1, board.getCommentCount());
    }

    @Test
    @DisplayName("게시글 댓글 수정 테스트")
    public void updateBoardCommentTest() {
        //given
        BoardCommentRequest request = getBoardCommentRequest();
        Long boardCommentId = boardCommentService.createBoardComment(member, board.getId(), request).getBoardCommentId();

        BoardCommentRequest updateRequest = new BoardCommentRequest("댓글 수정");

        //when
        boardCommentService.updateBoardComment(member, boardCommentId, updateRequest);
        BoardComment comment = boardCommentService.findBoardComment(boardCommentId);

        //then
        assertEquals("댓글 수정", comment.getContent());
    }

    @Test
    @DisplayName("게시글 댓글 삭제 테스트")
    public void deleteBoardCommentTest() {
        //given
        BoardCommentRequest request = getBoardCommentRequest();
        Long boardCommentId = boardCommentService.createBoardComment(member, board.getId(), request).getBoardCommentId();
        board = boardRepository.findById(board.getId()).get();
        int commentCount = board.getCommentCount();

        //when
        boardCommentService.deleteBoardComment(member, boardCommentId);
        Optional<BoardComment> comment = boardCommentRepository.findById(boardCommentId);

        //then
        board = boardRepository.findById(board.getId()).get();
        assertTrue(comment.isEmpty());
        assertEquals(commentCount - 1, board.getCommentCount());
    }

    @Test
    @DisplayName("게시글 댓글 좋아요/취소 토글 테스트")
    public void heartBoardCommentTest() {
        //1. 좋아요
        //given & when
        int heartCount = boardComment.getHeartCount();
        ToggleBoardCommentHeart liked = boardCommentService.toggleBoardCommentHeart(member, boardComment.getId());

        //BoardCommentInfos likedResponse = boardCommentService.getBoardCommentInfos(member, board.getId());
        //then
        boardComment = boardCommentRepository.findById(boardComment.getId()).get();
        assertEquals(boardComment.getId(), liked.getBoardCommentId());
        assertEquals(true, liked.getIsLiked());
        assertEquals(heartCount + 1, boardComment.getHeartCount());
        //assertEquals(true, likedResponse.getBoardCommentInfos().get(0).getIsLiked());

        //2. 좋아요 취소
        //given & when
        heartCount = boardComment.getHeartCount();
        ToggleBoardCommentHeart unLiked = boardCommentService.toggleBoardCommentHeart(member, boardComment.getId());
        //BoardCommentInfos unLikedResponse = boardCommentService.getBoardCommentInfos(member, board.getId());

        //then
        boardComment = boardCommentRepository.findById(boardComment.getId()).get();
        assertEquals(boardComment.getId(), unLiked.getBoardCommentId());
        assertEquals(false, unLiked.getIsLiked());
        assertEquals(heartCount - 1, boardComment.getHeartCount());
        //assertEquals(false, unLikedResponse.getBoardCommentInfos().get(0).getIsLiked());

    }

    @Test
    @DisplayName("게시판 댓글 리스트 조회 테스트")
    @Transactional
    public void getBoardCommentsTest() {
        //given
        PageRequest pageRequest = PageRequest.of(0, 10, Sort.by(Sort.Direction.DESC, "createdAt"));
        //when
        SliceResponse<BoardCommentInfo> response = boardCommentService.getBoardCommentInfos(member, board.getId(), null, pageRequest);

        //then
        assertEquals(member.getId(), response.getContent().get(0).getWriterId());
        assertEquals("게시글 댓글", response.getContent().get(0).getContent());
        assertEquals(0, response.getContent().get(0).getHeartCount());
        assertEquals(false, response.getContent().get(0).getIsLiked());
        assertEquals(true, response.getContent().get(0).getIsMine());
        assertEquals(10, response.getSize());
        assertEquals(1, response.getNumberOfElements());
    }
}