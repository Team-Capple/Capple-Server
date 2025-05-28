package com.server.capple.domain.answerComment.service;

import com.server.capple.domain.answerComment.dto.AnswerCommentRequest;
import com.server.capple.domain.answerComment.dto.AnswerCommentResponse;
import com.server.capple.domain.answerComment.entity.AnswerComment;
import com.server.capple.domain.notifiaction.service.NotificationService;
import com.server.capple.global.common.SliceResponse;
import com.server.capple.support.ServiceTestConfig;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@DisplayName("AnswerComment 서비스의 ")
@SpringBootTest
public class AnswerCommentServiceTest extends ServiceTestConfig {

    @Autowired
    private AnswerCommentService answerCommentService;
    @MockBean
    private NotificationService notificationService;

    @Test
    @DisplayName("답변 댓글 생성 테스트")
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
    public void deleteAnswerCommentTest() {
        //given
        AnswerCommentRequest request = getAnswerCommentRequest();
        Long answerCommentId = answerCommentService.createAnswerComment(member, answer.getId(), request).getAnswerCommentId();

        //when
        answerCommentService.deleteAnswerComment(member, answerCommentId);
        Optional<AnswerComment> comment = answerCommentRepository.findById(answerCommentId);

        //then
        assertTrue(comment.isEmpty());
    }

    @Test
    @DisplayName("답변 댓글 좋아요/취소 토글 테스트")
    public void toggleAnswerCommentHeartTest() {
        //1. 좋아요
        //given
        AnswerCommentResponse.AnswerCommentLike liked = answerCommentService.toggleAnswerCommentHeart(member, answerComment.getId());

        //then
        assertEquals(answerComment.getId(), liked.getAnswerCommentId());
        assertEquals(Boolean.TRUE, liked.getIsLiked());
        verify(notificationService, times(1)).sendAnswerCommentHeartNotification(any());

        //2. 좋아요 취소
        //given
        AnswerCommentResponse.AnswerCommentLike unLiked = answerCommentService.toggleAnswerCommentHeart(member, answerComment.getId());

        //then
        assertEquals(answerComment.getId(), unLiked.getAnswerCommentId());
        assertEquals(Boolean.FALSE, unLiked.getIsLiked());
        verify(notificationService, times(1)).sendAnswerCommentHeartNotification(any());
    }

    @Test
    @DisplayName("답변 댓글 조회 테스트")
    public void getAnswerCommentsTest() {
        //when
        SliceResponse<AnswerCommentResponse.AnswerCommentInfo> response =
                answerCommentService.getAnswerCommentInfos(answer.getId(), member.getId(), null, Pageable.ofSize(10));

        // then
        List<AnswerCommentResponse.AnswerCommentInfo> content = response.getContent();
        assertEquals(member.getId(), content.get(0).getWriterId());
        assertEquals("답변에 대한 댓글이어유", content.get(0).getContent());
        assertEquals(0, content.get(0).getHeartCount());
        assertFalse(content.get(0).getIsLiked());
        assertTrue(content.get(0).getIsMine());
    }

    @Test
    @DisplayName("좋아한 답변 댓글 조회 테스트")
    public void getAnswerCommentsWithHeartTest() {
        //given
        AnswerCommentResponse.AnswerCommentLike liked = answerCommentService.toggleAnswerCommentHeart(member, answerComment.getId());

        //when
        SliceResponse<AnswerCommentResponse.AnswerCommentInfo> response =
                answerCommentService.getAnswerCommentInfos(answer.getId(), member.getId(), null, Pageable.ofSize(10));


        //then
        assertEquals(1, response.getContent().get(0).getHeartCount());
        assertTrue(response.getContent().get(0).getIsLiked());
        assertTrue(response.getContent().get(0).getIsMine());
    }
}
