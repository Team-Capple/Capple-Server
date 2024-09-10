package com.server.capple.domain.boardComment.service;

import com.server.capple.domain.boardComment.dto.BoardCommentRequest;
import com.server.capple.domain.boardComment.dto.BoardCommentResponse.BoardCommentHeart;
import com.server.capple.domain.boardComment.dto.BoardCommentResponse.BoardCommentInfos;
import com.server.capple.domain.boardComment.entity.BoardComment;
import com.server.capple.support.ServiceTestConfig;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@DisplayName("BoardComment 서비스의 ")
@SpringBootTest
public class BoardCommentServiceTest extends ServiceTestConfig {

    @Autowired
    private BoardCommentService boardCommentService;

    @Test
    @DisplayName("게시글 댓글 생성 테스트")
    @Transactional
    public void createBoardCommentTest() {
        //given
        BoardCommentRequest request = getBoardCommentRequest();

        //when
        Long boardCommentId = boardCommentService.createBoardComment(member, board.getId(), request).getBoardCommentId();
        BoardComment comment = boardCommentService.findBoardComment(boardCommentId);

        //then
        assertEquals("게시글 댓글", comment.getContent());
    }

    @Test
    @DisplayName("게시글 댓글 수정 테스트")
    @Transactional
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
    @Transactional
    public void deleteBoardCommentTest() {
        //given
        BoardCommentRequest request = getBoardCommentRequest();
        Long boardCommentId = boardCommentService.createBoardComment(member, board.getId(), request).getBoardCommentId();

        //when
        boardCommentService.deleteBoardComment(member, boardCommentId);
        BoardComment comment = boardCommentService.findBoardComment(boardCommentId);

        //then
        assertNotNull(comment.getDeletedAt());
    }

    @Test
    @DisplayName("게시글 댓글 좋아요/취소 토글 테스트")
    @Transactional
    public void heartBoardCommentTest() {
        //1. 좋아요
        //given & when
        BoardCommentHeart liked = boardCommentService.heartBoardComment(member, boardComment.getId());
        BoardCommentInfos likedResponse = boardCommentService.getBoardCommentInfos(member, board.getId());
        //then
        assertEquals(boardComment.getId(), liked.getBoardCommentId());
        assertEquals(true, liked.getIsLiked());
        assertEquals(true, likedResponse.getBoardCommentInfos().get(0).getIsLiked());

        //2. 좋아요 취소
        //given & when
        BoardCommentHeart unLiked = boardCommentService.heartBoardComment(member, boardComment.getId());
        BoardCommentInfos unLikedResponse = boardCommentService.getBoardCommentInfos(member, board.getId());

        //then
        assertEquals(boardComment.getId(), unLiked.getBoardCommentId());
        assertEquals(false, unLiked.getIsLiked());
        assertEquals(false, unLikedResponse.getBoardCommentInfos().get(0).getIsLiked());

    }

    @Test
    @DisplayName("게시판 댓글 리스트 조회 테스트")
    @Transactional
    public void getBoardCommentsTest() {
        //when
        BoardCommentInfos response = boardCommentService.getBoardCommentInfos(member, board.getId());

        //then
        assertEquals(member.getId(), response.getBoardCommentInfos().get(0).getWriterId());
        assertEquals("게시글 댓글", response.getBoardCommentInfos().get(0).getContent());
        assertEquals(0L, response.getBoardCommentInfos().get(0).getHeartCount());
        assertEquals(false, response.getBoardCommentInfos().get(0).getIsLiked());
    }
}