package com.server.capple.domain.answerComment.service;

import com.server.capple.domain.answerComment.dto.AnswerCommentRequest;
import com.server.capple.domain.answerComment.dto.AnswerCommentResponse.*;
import com.server.capple.domain.answerComment.entity.AnswerComment;
import com.server.capple.support.ServiceTestConfig;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@DisplayName("AnswerComment 서비스의 ")
@SpringBootTest
public class AnswerCommentServiceTest extends ServiceTestConfig {

    @Autowired private AnswerCommentService answerCommentService;

    @Test
    @DisplayName("답변 댓글 생성 테스트")
    @Transactional
    public void createAnswerCommentTest() {
        //given
        AnswerCommentRequest request = getAnswerCommentRequest();

        //when
        Long answerCommentId = answerCommentService.createAnswerComment(member, answer.getId(), request).getAnswerCommentId();
        AnswerComment comment = answerCommentService.findAnswerComment(answerCommentId);

        //then
        assertEquals("댓글이 잘 달렸으면 좋겠어 . .", comment.getContent());
    }

    @Test
    @DisplayName("답변 댓글 수정 테스트")
    @Transactional
    public void updateAnswerCommentTest() {
        //given
        AnswerCommentRequest request = getAnswerCommentRequest();
        Long answerCommentId = answerCommentService.createAnswerComment(member, answer.getId(), request).getAnswerCommentId();

        AnswerCommentRequest updateRequest = AnswerCommentRequest.builder()
                .answerComment("댓글이 제대로 수정 되었으면 좋겠어")
                .build();

        //when
        answerCommentService.updateAnswerComment(member, answerCommentId, updateRequest);
        AnswerComment comment = answerCommentService.findAnswerComment(answerCommentId);

        //then
        assertEquals("댓글이 제대로 수정 되었으면 좋겠어", comment.getContent());
    }

    @Test
    @DisplayName("답변 댓글 삭제 테스트")
    @Transactional
    public void deleteAnswerCommentTest() {
        //given
        AnswerCommentRequest request = getAnswerCommentRequest();
        Long answerCommentId = answerCommentService.createAnswerComment(member, answer.getId(), request).getAnswerCommentId();

        //when
        answerCommentService.deleteAnswerComment(member, answerCommentId);
        AnswerComment comment = answerCommentService.findAnswerComment(answerCommentId);

        //then
        assertNotNull(comment.getDeletedAt());

    }

    @Test
    @DisplayName("답변 댓글 좋아요/취소 토글 테스트")
    @Transactional
    public void heartAnswerCommentTest() {
        //1. 좋아요
        //given
        AnswerCommentHeart liked = answerCommentService.heartAnswerComment(member, answerComment.getId());

        //then
        assertEquals(answerComment.getId(), liked.getAnswerCommentId());
        assertEquals(Boolean.TRUE, liked.getIsLiked());

        //2. 좋아요 취소
        //given
        AnswerCommentHeart unLiked = answerCommentService.heartAnswerComment(member, answerComment.getId());

        //then
        assertEquals(answerComment.getId(), unLiked.getAnswerCommentId());
        assertEquals(Boolean.FALSE, unLiked.getIsLiked());

    }

    @Test
    @DisplayName("답변 댓글 조회 테스트")
    @Transactional
    public void getAnswerCommentsTest() {
        //when
        AnswerCommentInfos response = answerCommentService.getAnswerCommentInfos(answer.getId());

        //then
        assertEquals(member.getId(), response.getAnswerCommentInfos().get(0).getWriterId());
        assertEquals("답변에 대한 댓글이어유", response.getAnswerCommentInfos().get(0).getContent());
        assertEquals(0L, response.getAnswerCommentInfos().get(0).getHeartCount());
    }

    @Test
    @DisplayName("좋아한 답변 댓글 조회 테스트")
    @Transactional
    public void getAnswerCommentsWithHeartTest() {
        //given
        AnswerCommentHeart liked = answerCommentService.heartAnswerComment(member, answerComment.getId());

        //when
        AnswerCommentInfos response = answerCommentService.getAnswerCommentInfos(answer.getId());

        //then
        assertEquals(1L, response.getAnswerCommentInfos().get(0).getHeartCount());
    }
}
