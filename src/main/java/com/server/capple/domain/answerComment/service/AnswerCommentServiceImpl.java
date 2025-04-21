package com.server.capple.domain.answerComment.service;

import com.server.capple.domain.answer.entity.Answer;
import com.server.capple.domain.answer.service.AnswerService;
import com.server.capple.domain.answerComment.dto.AnswerCommentRequest;
import com.server.capple.domain.answerComment.dto.AnswerCommentResponse.*;
import com.server.capple.domain.answerComment.entity.AnswerComment;
import com.server.capple.domain.answerComment.mapper.AnswerCommentMapper;
import com.server.capple.domain.answerComment.repository.AnswerCommentHeartRedisRepository;
import com.server.capple.domain.answerComment.repository.AnswerCommentRepository;
import com.server.capple.domain.answerSubscribeMember.service.AnswerSubscribeMemberService;
import com.server.capple.domain.member.entity.Member;
import com.server.capple.domain.member.service.MemberService;
import com.server.capple.domain.notifiaction.service.NotificationService;
import com.server.capple.global.exception.RestApiException;
import com.server.capple.global.exception.errorCode.CommentErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Service
@RequiredArgsConstructor
public class AnswerCommentServiceImpl implements AnswerCommentService{

    private final AnswerCommentRepository answerCommentRepository;
    private final AnswerCommentHeartRedisRepository answerCommentHeartRedisRepository;
    private final AnswerCommentMapper answerCommentMapper;
    private final MemberService memberService;
    private final AnswerService answerService;
    private final NotificationService notificationService;
    private final AnswerSubscribeMemberService answerSubscribeMemberService;

    /* 댓글 작성 */
    @Override
    @Transactional
    public AnswerCommentId createAnswerComment(Member member, Long answerId, AnswerCommentRequest request) {
        Member loginMember = memberService.findMember(member.getId());
        Answer answer = answerService.findAnswer(answerId);
        AnswerComment answerComment = answerCommentRepository.save(answerCommentMapper.toAnswerCommentEntity(loginMember, answer, request.getAnswerComment()));
        notificationService.sendAnswerCommentNotification(answer, answerComment);
        return new AnswerCommentId(answerComment.getId());
    }

    /* 댓글 삭제 */
    @Override
    @Transactional
    public AnswerCommentId deleteAnswerComment(Member member, Long commentId) {
        AnswerComment answerComment = findAnswerComment(commentId);
        checkPermission(member, answerComment); // 유저 권한 체크

        answerSubscribeMemberService.deleteAnswerSubscribeMemberByAnswerId(answerComment.getAnswer().getId());
        answerComment.delete();
        return new AnswerCommentId(answerComment.getId());
    }

    /* 댓글 수정 */
    @Override
    @Transactional
    public AnswerCommentId updateAnswerComment(Member member, Long commentId, AnswerCommentRequest request) {
        AnswerComment answerComment = findAnswerComment(commentId);
        checkPermission(member, answerComment); // 유저 권한 체크

        answerComment.update(request.getAnswerComment());
        return new AnswerCommentId(commentId);
    }

    /* 댓글 좋아요/취소 */
    @Override
    @Transactional
    public AnswerCommentHeart heartAnswerComment(Member member, Long commentId) {
        Boolean isLiked = answerCommentHeartRedisRepository.toggleAnswerCommentHeart(commentId, member.getId());
        return new AnswerCommentHeart(commentId, isLiked);
    }

    /* 답변에 대한 댓글 조회 */
    @Override
    public AnswerCommentInfos getAnswerCommentInfos(Long answerId) {
        List<AnswerCommentInfo> commentInfos = answerCommentRepository.findAnswerCommentByAnswerId(answerId).stream()
                .map(comment -> {
                    Long heartCount = answerCommentHeartRedisRepository.getAnswerCommentHeartsCount(comment.getId());
                    return answerCommentMapper.toAnswerCommentInfo(comment, heartCount);
                })
                .toList();

        return new AnswerCommentInfos(commentInfos);
    }

    private void checkPermission(Member member, AnswerComment answerComment) {
        Member loginMember = memberService.findMember(member.getId());

        if (!loginMember.getId().equals(answerComment.getMember().getId()))
            throw new RestApiException(CommentErrorCode.COMMENT_NOT_FOUND);
    }

    @Override
    public AnswerComment findAnswerComment(Long answerCommentId) {
        return answerCommentRepository.findById(answerCommentId).orElseThrow(
                () -> new RestApiException(CommentErrorCode.COMMENT_NOT_FOUND)
        );
    }
}
