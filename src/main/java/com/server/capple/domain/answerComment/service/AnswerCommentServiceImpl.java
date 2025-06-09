package com.server.capple.domain.answerComment.service;

import com.server.capple.domain.answer.dao.AnswerRDBDao;
import com.server.capple.domain.answer.entity.Answer;
import com.server.capple.domain.answer.service.AnswerConcurrentService;
import com.server.capple.domain.answer.service.AnswerService;
import com.server.capple.domain.answer.service.AnswerServiceImpl;
import com.server.capple.domain.answerComment.dao.AnswerCommentRDBDao;
import com.server.capple.domain.answerComment.dto.AnswerCommentRequest;
import com.server.capple.domain.answerComment.dto.AnswerCommentResponse.*;
import com.server.capple.domain.answerComment.entity.AnswerComment;
import com.server.capple.domain.answerComment.entity.AnswerCommentHeart;
import com.server.capple.domain.answerComment.mapper.AnswerCommentHeartMapper;
import com.server.capple.domain.answerComment.mapper.AnswerCommentMapper;
import com.server.capple.domain.answerComment.repository.AnswerCommentHeartRedisRepository;
import com.server.capple.domain.answerComment.repository.AnswerCommentHeartRepository;
import com.server.capple.domain.answerComment.repository.AnswerCommentRepository;
import com.server.capple.domain.answerSubscribeMember.service.AnswerSubscribeMemberService;
import com.server.capple.domain.member.entity.Member;
import com.server.capple.domain.member.service.MemberService;
import com.server.capple.domain.notifiaction.service.NotificationService;
import com.server.capple.global.common.SliceResponse;
import com.server.capple.global.exception.RestApiException;
import com.server.capple.global.exception.errorCode.CommentErrorCode;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;


@Service
@RequiredArgsConstructor
public class AnswerCommentServiceImpl implements AnswerCommentService{

    private final AnswerCommentRepository answerCommentRepository;
    private final AnswerCommentHeartRepository answerCommentHeartRepository;
    private final AnswerCommentMapper answerCommentMapper;
    private final MemberService memberService;
    private final AnswerService answerService;
    private final NotificationService notificationService;
    private final AnswerSubscribeMemberService answerSubscribeMemberService;
    private final AnswerCommentConcurrentService answerCommentConcurrentService;
    private final AnswerConcurrentService answerConcurrentService;
    private final AnswerCommentCountService answerCommentCountService;
    private final AnswerCommentHeartMapper answerCommentHeartMapper;
    private final ApplicationEventPublisher applicationEventPublisher;

    /* 댓글 작성 */
    @Override
    @Transactional
    public AnswerCommentId createAnswerComment(Member member, Long answerId, AnswerCommentRequest request) {
        Member loginMember = memberService.findMember(member.getId());
        Answer answer = answerService.findAnswer(answerId);
        AnswerComment answerComment = answerCommentRepository.save(answerCommentMapper.toAnswerCommentEntity(loginMember, answer, request.getAnswerComment()));
        applicationEventPublisher.publishEvent(new AnswerCommentCountChangedEvent(answerId, loginMember));
        notificationService.sendAnswerCommentNotification(answer, answerComment);
        if (!answerConcurrentService.increaseCommentCount(answer)) { // 답변 commentCount 증가
            throw new RestApiException(CommentErrorCode.COMMENT_COUNT_INCREASE_FAILED);
        }
        return new AnswerCommentId(answerComment.getId());
    }

    /* 댓글 삭제 */
    @Override
    @Transactional
    public AnswerCommentId deleteAnswerComment(Member member, Long commentId) {
        AnswerComment answerComment = findAnswerComment(commentId);
        checkPermission(member, answerComment); // 유저 권한 체크
        applicationEventPublisher.publishEvent(new AnswerCommentCountChangedEvent(answerComment.getAnswer().getId(), member));
        answerSubscribeMemberService.deleteAnswerSubscribeMemberByAnswerId(answerComment.getAnswer().getId());
        if (!answerConcurrentService.decreaseCommentCount(answerComment.getAnswer())) { // 답변 commentCount 감소
            throw new RestApiException(CommentErrorCode.COMMENT_COUNT_DECREASE_FAILED);
        }
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

    @Override
    @Transactional
    public AnswerCommentLike toggleAnswerCommentHeart(Member loginMember, Long commentId) {
        Member member = memberService.findMember(loginMember.getId());
        AnswerComment answerComment = findAnswerComment(commentId);

        // 좋아요 여부 확인 (없으면 새로 저장)
        AnswerCommentHeart answerCommentHeart = answerCommentHeartRepository.findByMemberAndAnswerComment(member, answerComment)
                .orElseGet(() -> {
                    AnswerCommentHeart newHeart = answerCommentHeartMapper.toAnswerCommentHeart(answerComment, member);
                    return answerCommentHeartRepository.save(newHeart);
                });

        boolean isLiked = answerCommentHeart.toggleHeart();

        if (!answerCommentConcurrentService.setHeartCount(answerComment, isLiked)) {
            throw new RestApiException(CommentErrorCode.COMMENT_HEART_CHANGE_FAILED);
        }

        if (isLiked) {
            notificationService.sendAnswerCommentHeartNotification(answerComment);
        }

        return new AnswerCommentLike(commentId, isLiked);
    }

    @Override
    public SliceResponse<AnswerCommentInfo> getAnswerCommentInfos(Long answerId, Long memberId, Long lastIndex, Pageable pageable) {

        Member member = memberService.findMember(memberId);
        Slice<AnswerCommentRDBDao.AnswerCommentInfoInterface> answerCommentSliceInterfaces = answerCommentRepository.findAnswerCommentByAnswerId(answerId, member, lastIndex, pageable);

        lastIndex = getLastIndexFromAnswerCommentInfoInterface(answerCommentSliceInterfaces);

        return SliceResponse.toSliceResponse(answerCommentSliceInterfaces, answerCommentSliceInterfaces.getContent().stream().map(
                answerCommentInfoDto -> answerCommentMapper.toAnswerCommentInfo(
                        answerCommentInfoDto,
                        memberId
                )
        ).toList(), lastIndex.toString(), answerCommentCountService.getAnswerCommentCount(answerId));

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

    private Long getLastIndexFromAnswerCommentInfoInterface(Slice<AnswerCommentRDBDao.AnswerCommentInfoInterface> answerCommentInfoSliceInterface) {
        if(answerCommentInfoSliceInterface.hasContent())
            return answerCommentInfoSliceInterface.stream().map(AnswerCommentRDBDao.AnswerCommentInfoInterface::getAnswerComment).map(AnswerComment::getId).min(Long::compareTo).get();
        return -1L;
    }

    @Getter
    @AllArgsConstructor
    static class AnswerCommentCountChangedEvent {
        private Long answerId;
        private Member member;
    }

    @TransactionalEventListener(classes = AnswerCommentServiceImpl.AnswerCommentCountChangedEvent.class, phase = TransactionPhase.AFTER_COMPLETION)
    public void handleAnswerCommentCountChangedEvent(AnswerCommentServiceImpl.AnswerCommentCountChangedEvent event) {
        answerCommentCountService.updateAnswerCommentCount(event.getAnswerId());
    }
}
