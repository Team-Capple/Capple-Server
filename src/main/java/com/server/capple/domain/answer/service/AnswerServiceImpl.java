package com.server.capple.domain.answer.service;

import com.server.capple.domain.answer.dao.AnswerRDBDao.AnswerInfoInterface;
import com.server.capple.domain.answer.dto.AnswerRequest;
import com.server.capple.domain.answer.dto.AnswerResponse;
import com.server.capple.domain.answer.dto.AnswerResponse.AnswerInfo;
import com.server.capple.domain.answer.dto.AnswerResponse.AnswerLike;
import com.server.capple.domain.answer.dto.AnswerResponse.MemberAnswerInfo;
import com.server.capple.domain.answer.entity.Answer;
import com.server.capple.domain.answer.mapper.AnswerMapper;
import com.server.capple.domain.answer.repository.AnswerHeartRedisRepository;
import com.server.capple.domain.answer.repository.AnswerRepository;
import com.server.capple.domain.member.entity.Member;
import com.server.capple.domain.member.service.MemberService;
import com.server.capple.domain.question.entity.Question;
import com.server.capple.domain.question.service.QuestionService;
import com.server.capple.domain.report.repository.ReportRepository;
import com.server.capple.global.common.SliceResponse;
import com.server.capple.global.exception.RestApiException;
import com.server.capple.global.exception.errorCode.AnswerErrorCode;
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
@Transactional(readOnly = true)
public class AnswerServiceImpl implements AnswerService {
    private final AnswerRepository answerRepository;
    private final ReportRepository reportRepository;
    private final QuestionService questionService;
    private final AnswerMapper answerMapper;
    private final MemberService memberService;
    private final AnswerHeartRedisRepository answerHeartRedisRepository;
    private final AnswerCountService answerCountService;
    private final ApplicationEventPublisher applicationEventPublisher;

    @Transactional
    @Override
    public AnswerResponse.AnswerId createAnswer(Member loginMember, Long questionId, AnswerRequest request) {

        Member member = memberService.findMember(loginMember.getId());
        Question question = questionService.findQuestion(questionId);

        if (answerRepository.existsByQuestionAndMember(question, loginMember)) {
            throw new RestApiException(AnswerErrorCode.ANSWER_ALREADY_EXIST);
        }

        //답변 저장
        Answer answer = answerRepository.save(answerMapper.toAnswerEntity(request, member, question));
//        answer.getQuestion().increaseCommentCount();
        applicationEventPublisher.publishEvent(new QuestionAnswerCountChangedEvent(questionId));
        return new AnswerResponse.AnswerId(answer.getId());
    }

    @Transactional
    @Override
    public AnswerResponse.AnswerId updateAnswer(Member loginMember, Long answerId, AnswerRequest request) {
        Answer answer = findAnswer(answerId);

        checkPermission(loginMember, answer);

        //답변 update
        answer.update(request);

        return new AnswerResponse.AnswerId(answerId);

    }

    @Override
    @Transactional
    public AnswerResponse.AnswerId deleteAnswer(Member loginMember, Long answerId) {
        Answer answer = findAnswer(answerId);
        applicationEventPublisher.publishEvent(new QuestionAnswerCountChangedEvent(answer.getQuestion().getId()));

        checkPermission(loginMember, answer);
//        answer.getQuestion().decreaseCommentCount();

        answer.delete();
        return new AnswerResponse.AnswerId(answerId);
    }


    //답변 좋아요 / 취소
    @Override
    public AnswerLike toggleAnswerHeart(Member loginMember, Long answerId) {
        Member member = memberService.findMember(loginMember.getId());
        answerRepository.findById(answerId).orElseThrow(() -> new RestApiException(AnswerErrorCode.ANSWER_NOT_FOUND));
        Boolean isLiked = answerHeartRedisRepository.toggleAnswerHeart(member.getId(), answerId);
        return new AnswerLike(answerId, isLiked);
    }

    @Override
    public SliceResponse<AnswerInfo> getAnswerList(Long memberId, Long questionId, Long lastIndex, Pageable pageable) {
        Slice<AnswerInfoInterface> answerInfoSliceInterface = answerRepository.findByQuestion(questionId, lastIndex, pageable);
        lastIndex = getLastIndexFromAnswerInfoInterface(answerInfoSliceInterface);
        return SliceResponse.toSliceResponse(answerInfoSliceInterface, answerInfoSliceInterface.getContent().stream().map(
            answerInfoDto -> answerMapper.toAnswerInfo(
                answerInfoDto,
                memberId,
                answerHeartRedisRepository.isMemberLikedAnswer(memberId, answerInfoDto.getAnswer().getId())
            )
        ).toList(), lastIndex.toString(), answerCountService.getQuestionAnswerCount(questionId));
    }

    // 유저가 작성한 답변 조회
    @Override
    public SliceResponse<MemberAnswerInfo> getMemberAnswer(Member member, Long lastIndex, Pageable pageable) {
        Slice<Answer> answerSlice = answerRepository.findByMemberAndIdIsLessThan(member, lastIndex, pageable);
        lastIndex = getLastIndexFromAnswer(answerSlice);
        return SliceResponse.toSliceResponse(
            answerSlice, answerSlice.getContent().stream()
                .map(answer -> answerMapper.toMemberAnswerInfo(
                    answer,
                    answerHeartRedisRepository.getAnswerHeartsCount(answer.getId()),
                    answerHeartRedisRepository.isMemberLikedAnswer(member.getId(), answer.getId())
                )).toList(), lastIndex.toString(), null
        );
    }

    // 유저가 좋아한 답변 조회 //TODO 좋아요니까 좋아요한 순으로 정렬해야할거같은데 Answer의 createAt으로 하고 있음
    @Override
    public SliceResponse<MemberAnswerInfo> getMemberHeartAnswer(Member member, Long lastIndex, Pageable pageable) {
        Slice<Answer> answerSlice = answerRepository.findByIdInAndIdIsLessThan(answerHeartRedisRepository.getMemberHeartsAnswer(member.getId()), lastIndex, pageable);
        lastIndex = getLastIndexFromAnswer(answerSlice);
        return SliceResponse.toSliceResponse(answerSlice, answerSlice.getContent().stream()
            .map(answer -> answerMapper.toMemberAnswerInfo(
                answer,
                answerHeartRedisRepository.getAnswerHeartsCount(answer.getId()),
                answerHeartRedisRepository.isMemberLikedAnswer(member.getId(), answer.getId())
            )).toList(), lastIndex.toString(), null
        );
    }

    //로그인된 유저와 작성자가 같은지 체크
    private void checkPermission(Member loginMember, Answer answer) {
        Member member = memberService.findMember(loginMember.getId());

        if (!member.getId().equals(answer.getMember().getId()))
            throw new RestApiException(AnswerErrorCode.ANSWER_UNAUTHORIZED);
    }

    @Override
    public Answer findAnswer(Long answerId) {
        return answerRepository.findById(answerId).orElseThrow(
            () -> new RestApiException(AnswerErrorCode.ANSWER_NOT_FOUND)
        );
    }

    private Long getLastIndexFromAnswerInfoInterface(Slice<AnswerInfoInterface> answerInfoSliceInterface) {
        if(answerInfoSliceInterface.hasContent())
            return answerInfoSliceInterface.stream().map(AnswerInfoInterface::getAnswer).map(Answer::getId).min(Long::compareTo).get();
        return -1L;
    }

    private Long getLastIndexFromAnswer(Slice<Answer> answerSlice) {
        if (answerSlice.hasContent())
            return answerSlice.stream().map(Answer::getId).min(Long::compareTo).get();
        return -1L;
    }

    @Getter
    @AllArgsConstructor
    static class QuestionAnswerCountChangedEvent {
        private Long questionId;
    }

    @TransactionalEventListener(classes = QuestionAnswerCountChangedEvent.class, phase = TransactionPhase.AFTER_COMPLETION)
    public void handleQuestionCreatedEvent(QuestionAnswerCountChangedEvent event) {
        answerCountService.updateQuestionAnswerCount(event.getQuestionId());
    }
}
