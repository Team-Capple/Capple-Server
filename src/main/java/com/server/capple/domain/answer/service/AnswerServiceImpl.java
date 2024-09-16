package com.server.capple.domain.answer.service;

import com.server.capple.domain.answer.dao.AnswerRDBDao.AnswerInfoInterface;
import com.server.capple.domain.answer.dto.AnswerRequest;
import com.server.capple.domain.answer.dto.AnswerResponse;
import com.server.capple.domain.answer.dto.AnswerResponse.AnswerInfo;
import com.server.capple.domain.answer.dto.AnswerResponse.AnswerLike;
import com.server.capple.domain.answer.dto.AnswerResponse.MemberAnswerList;
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
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

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

        checkPermission(loginMember, answer);
//        answer.getQuestion().decreaseCommentCount();


        answer.delete();

        return new AnswerResponse.AnswerId(answerId);
    }


    //답변 좋아요 / 취소
    @Override
    public AnswerLike toggleAnswerHeart(Member loginMember, Long answerId) {
        Member member = memberService.findMember(loginMember.getId());

        Boolean isLiked = answerHeartRedisRepository.toggleAnswerHeart(member.getId(), answerId);
        return new AnswerLike(answerId, isLiked);
    }

    @Override
    public SliceResponse<AnswerInfo> getAnswerList(Long memberId, Long questionId, Pageable pageable) {
        Slice<AnswerInfoInterface> answerInfoSliceInterface = answerRepository.findByQuestion(questionId, pageable).orElseThrow(()
            -> new RestApiException(AnswerErrorCode.ANSWER_NOT_FOUND));
        return answerMapper.toAnswerInfoSliceResponse(answerInfoSliceInterface, answerInfoSliceInterface.getContent().stream().map(
            answerInfoDto -> answerMapper.toAnswerInfo(
                answerInfoDto.getAnswer(),
                memberId,
                answerInfoDto.getIsReported(),
                answerHeartRedisRepository.isMemberLikedAnswer(memberId, answerInfoDto.getAnswer().getId()),
                answerInfoDto.getAnswer().getMember().getId().equals(memberId)
            )
        ).toList());
    }

    // 유저가 작성한 답변 조회
    @Override
    public MemberAnswerList getMemberAnswer(Member member) {
        List<Answer> answers = answerRepository.findByMember(member).orElse(null);
        return answerMapper.toMemberAnswerList(
            answers.stream()
                .map(answer -> answerMapper.toMemberAnswerInfo(answer, answerHeartRedisRepository.getAnswerHeartsCount(answer.getId()), answerHeartRedisRepository.isMemberLikedAnswer(member.getId(), answer.getId())))
                .toList()
        );
    }

    // 유저가 좋아한 답변 조회
    @Override
    public MemberAnswerList getMemberHeartAnswer(Member member) {
        return answerMapper.toMemberAnswerList(
            answerHeartRedisRepository.getMemberHeartsAnswer(member.getId())
                .stream()
                .map(answerId -> answerMapper.toMemberAnswerInfo(findAnswer((answerId)), answerHeartRedisRepository.getAnswerHeartsCount(answerId), answerHeartRedisRepository.isMemberLikedAnswer(member.getId(), answerId)))
                .toList()
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
}
