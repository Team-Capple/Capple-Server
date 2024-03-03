package com.server.capple.domain.answer.service;

import com.server.capple.domain.answer.dto.AnswerRequest;
import com.server.capple.domain.answer.dto.AnswerResponse;
import com.server.capple.domain.answer.dto.AnswerResponse.AnswerList;
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
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
        if (question.getQuestionStatus().equals(QuestionStatus.LIVE))
            tagService.saveQuestionTags(questionId, request.getTags());

        return new AnswerResponse.AnswerId(answer.getId());
    }

    @Override
    public Answer findAnswer(Long answerId) {
        return answerRepository.findById(answerId).orElseThrow(
                () -> new RestApiException(AnswerErrorCode.ANSWER_NOT_FOUND)
        );
    }

    @Override
    public AnswerList getAnswerList(Long questionId, int numberOfAnswer) {
        Pageable pageable = PageRequest.of(0, numberOfAnswer);

        return answerMapper.toAnswerList(
                answerRepository.findByQuestion(questionId, pageable).orElseThrow(()
                -> new RestApiException(AnswerErrorCode.ANSWER_NOT_FOUND))
                        .stream()
                        .map(answerMapper::toAnswerInfo)
                        .toList());
    }

}
