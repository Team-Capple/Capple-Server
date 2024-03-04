package com.server.capple.domain.answer.service;

import com.server.capple.domain.answer.dto.AnswerRequest;
import com.server.capple.domain.answer.dto.AnswerResponse;
import com.server.capple.domain.answer.entity.Answer;
import com.server.capple.domain.answer.mapper.AnswerMapper;
import com.server.capple.domain.answer.repository.AnswerRepository;
import com.server.capple.domain.member.entity.Member;
import com.server.capple.domain.member.service.MemberService;
import com.server.capple.domain.question.entity.Question;
import com.server.capple.domain.question.entity.QuestionStatus;
import com.server.capple.domain.question.service.QuestionService;
import com.server.capple.domain.tag.service.TagService;
import com.server.capple.global.exception.RestApiException;
import com.server.capple.global.exception.errorCode.AnswerErrorCode;
import lombok.RequiredArgsConstructor;
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
    private final QuestionService questionService;
    private final AnswerMapper answerMapper;
    private final MemberService memberService;
    private final TagService tagService;

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
        if (question.getQuestionStatus().equals(QuestionStatus.ONGOING))
            tagService.saveQuestionTags(questionId, request.getTags());

        return new AnswerResponse.AnswerId(answer.getId());
    }

    @Transactional
    @Override
    public AnswerResponse.AnswerId updateAnswer(Member loginMember, Long answerId, AnswerRequest request) {
        Answer answer = findAnswer(answerId);
        Question question = answer.getQuestion();
        checkPermission(loginMember, answer);

        //현재 답변의 태그들
        List<String> answerTags = Arrays.stream(answer.getTags().split(" "))
                .filter(tag -> !tag.isEmpty()).toList();

        //rdb에 태그 update
        request.getTags().forEach(tagService::findOrCreateTag);

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
        if (question.getQuestionStatus().equals(QuestionStatus.ONGOING))
            tagService.updateQuestionTags(question.getId(), addedTags, removedTags);

        answer.update(request);

        return new AnswerResponse.AnswerId(answerId);

    }

    @Transactional
    @Override
    public AnswerResponse.AnswerId deleteAnswer(Member loginMember, Long answerId) {
        Answer answer = findAnswer(answerId);
        Question question = answer.getQuestion();
        checkPermission(loginMember, answer);

        List<String> answerTags = Arrays.stream(answer.getTags().split(" "))
                .filter(tag -> !tag.isEmpty()).toList();

        //redis tag 삭제
        tagService.deleteTags(answerTags);

        //온에어 시간이 지나면 순위에는 변동 X 온에어 시간일 경우에만 변동
        //redis 질문별 tag 삭제
        if (question.getQuestionStatus().equals(QuestionStatus.ONGOING))
            tagService.deleteQuestionTags(question.getId(), answerTags);

        answer.delete();

        return new AnswerResponse.AnswerId(answerId);
    }

    @Override
    @Transactional
    public AnswerResponse.AnswerId toggleAnswerHeart(Member loginMember, Long answerId) {
        return new AnswerResponse.AnswerId(answerId);
    }

    @Override
    public Answer findAnswer(Long answerId) {
        return answerRepository.findById(answerId).orElseThrow(
                () -> new RestApiException(AnswerErrorCode.ANSWER_NOT_FOUND)
        );
    }

    private void checkPermission(Member loginMember, Answer answer) {
        Member member = memberService.findMember(loginMember.getId());

        if (!member.getId().equals(answer.getMember().getId()))
            throw new RestApiException(AnswerErrorCode.ANSWER_UNAUTHORIZED);
    }

}
