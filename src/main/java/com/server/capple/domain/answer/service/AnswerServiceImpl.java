package com.server.capple.domain.answer.service;

import com.server.capple.domain.answer.dto.AnswerRequest;
import com.server.capple.domain.answer.dto.AnswerResponse;
import com.server.capple.domain.answer.dto.AnswerResponse.MemberAnswerList;
import com.server.capple.domain.answer.dto.AnswerResponse.AnswerList;
import com.server.capple.domain.answer.entity.Answer;
import com.server.capple.domain.answer.mapper.AnswerMapper;
import com.server.capple.domain.answer.repository.AnswerHeartRedisRepository;
import com.server.capple.domain.answer.repository.AnswerRepository;
import com.server.capple.domain.member.entity.Member;
import com.server.capple.domain.member.service.MemberService;
import com.server.capple.domain.question.entity.Question;
import com.server.capple.domain.question.entity.QuestionStatus;
import com.server.capple.domain.question.service.QuestionService;
import com.server.capple.domain.report.repository.ReportRepository;
import com.server.capple.domain.tag.service.TagService;
import com.server.capple.global.exception.RestApiException;
import com.server.capple.global.exception.errorCode.AnswerErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Arrays;
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
    private final TagService tagService;
    private final AnswerHeartRedisRepository answerHeartRedisRepository;

    @Transactional
    @Override
    public AnswerResponse.AnswerId createAnswer(Member loginMember, Long questionId, AnswerRequest request) {

        Member member = memberService.findMember(loginMember.getId());
        Question question = questionService.findQuestion(questionId);

        //답변 저장
        Answer answer = answerRepository.save(answerMapper.toAnswerEntity(request, member, question));

        //rdb에 태그 저장
        request.getTags().forEach(tagService::findOrCreateTag);

        //redis에 태그 저장
        tagService.saveTags(request.getTags());

        //온에어 질문일 경우, redis 질문 별 태그 저장
        if (question.getQuestionStatus().equals(QuestionStatus.LIVE))
            tagService.saveQuestionTags(questionId, request.getTags());

        return new AnswerResponse.AnswerId(answer.getId());
    }

    @Transactional
    @Override
    public AnswerResponse.AnswerId updateAnswer(Member loginMember, Long answerId, AnswerRequest request) {
        Answer answer = findAnswer(answerId);
        Question question = answer.getQuestion();
        checkPermission(loginMember, answer);

        //rdb에 태그 update
        request.getTags().forEach(tagService::findOrCreateTag);

        //현재 답변의 태그들
        List<String> answerTags = getCurrentAnswerTags(answer);

        //추가된 태그들
        List<String> addedTags = new ArrayList<>(request.getTags());
        addedTags.removeAll(answerTags);

        //삭제된 태그들
        List<String> removedTags = new ArrayList<>(answerTags);
        removedTags.removeAll(request.getTags());

        //redis 태그 update
        tagService.updateTags(addedTags, removedTags);

        //온에어 시간이 지나면 순위에는 변동 X 온에어 시간일 경우에만 변동
        //redis 질문별 태그 update
        if (question.getQuestionStatus().equals(QuestionStatus.LIVE))
            tagService.updateQuestionTags(question.getId(), addedTags, removedTags);

        //답변 update
        answer.update(request);

        return new AnswerResponse.AnswerId(answerId);

    }

    @Override
    @Transactional
    public AnswerResponse.AnswerId deleteAnswer(Member loginMember, Long answerId) {
        Answer answer = findAnswer(answerId);
        Question question = answer.getQuestion();
        checkPermission(loginMember, answer);

        //현재 답변의 태그들
        List<String> answerTags = getCurrentAnswerTags(answer);

        //redis tag 삭제
        tagService.deleteTags(answerTags);

        //온에어 시간이 지나면 순위에는 변동 X 온에어 시간일 경우에만 변동
        //redis 질문별 tag 삭제
        if (question.getQuestionStatus().equals(QuestionStatus.LIVE))
            tagService.deleteQuestionTags(question.getId(), answerTags);

        answer.delete();

        return new AnswerResponse.AnswerId(answerId);
    }


    //답변 좋아요 / 취소
    @Override
    public AnswerResponse.AnswerLike toggleAnswerHeart(Member loginMember, Long answerId) {
        Member member = memberService.findMember(loginMember.getId());

        Boolean isLiked = answerHeartRedisRepository.toggleAnswerHeart(member.getId(), answerId);
        return new AnswerResponse.AnswerLike(answerId, isLiked);
    }

    @Override
    public AnswerList getAnswerList(Long memberId, Long questionId, String keyword, Pageable pageable) {

        if (keyword == null) {
            return answerMapper.toAnswerList(
                    answerRepository.findByQuestion(questionId, pageable).orElseThrow(()
                                    -> new RestApiException(AnswerErrorCode.ANSWER_NOT_FOUND))
                            .stream()
                            .map(answer -> answerMapper.toAnswerInfo(answer, memberId, reportRepository.existsReportByAnswer(answer)))
                            .toList());
        } else {
            return answerMapper.toAnswerList(
                    answerRepository.findByQuestionAndKeyword(questionId, keyword, pageable).orElseThrow(()
                                    -> new RestApiException(AnswerErrorCode.ANSWER_NOT_FOUND))
                            .stream()
                            .map(answer -> answerMapper.toAnswerInfo(answer, memberId, reportRepository.existsReportByAnswer(answer)))
                            .toList());
        }

    }

    @Override
    public MemberAnswerList getMemberAnswer(Member member) {
        List<Answer> answers = answerRepository.findByMember(member).orElse(null);
        return answerMapper.toMemberAnswerList(
                answers.stream()
                        .map(answer -> answerMapper.toMemberAnswerInfo(answer, answerHeartRedisRepository.getAnswerHeartsCount(answer.getId())))
                        .toList()
        );
    }

    @Override
    public MemberAnswerList getMemberHeartAnswer(Member member) {
        return answerMapper.toMemberAnswerList(
                answerHeartRedisRepository.getMemberHeartsAnswer(member.getId())
                        .stream()
                        .map(answerId -> answerMapper.toMemberAnswerInfo(findAnswer(Long.valueOf((answerId))), answerHeartRedisRepository.getAnswerHeartsCount(Long.valueOf(answerId))))
                        .toList()
        );
    }


    //로그인된 유저와 작성자가 같은지 체크
    private void checkPermission(Member loginMember, Answer answer) {
        Member member = memberService.findMember(loginMember.getId());

        if (!member.getId().equals(answer.getMember().getId()))
            throw new RestApiException(AnswerErrorCode.ANSWER_UNAUTHORIZED);
    }

    private List<String> getCurrentAnswerTags(Answer answer) {
        return Arrays.stream(answer.getTags().split(" "))
                .filter(tag -> !tag.isEmpty()).toList();
    }

    @Override
    public Answer findAnswer(Long answerId) {
        return answerRepository.findById(answerId).orElseThrow(
                () -> new RestApiException(AnswerErrorCode.ANSWER_NOT_FOUND)
        );
    }
}
