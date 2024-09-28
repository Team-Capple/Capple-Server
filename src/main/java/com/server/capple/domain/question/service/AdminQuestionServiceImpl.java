package com.server.capple.domain.question.service;

import com.server.capple.domain.question.dto.request.QuestionRequest.QuestionCreate;
import com.server.capple.domain.question.dto.response.QuestionResponse.QuestionId;
import com.server.capple.domain.question.entity.Question;
import com.server.capple.domain.question.entity.QuestionStatus;
import com.server.capple.domain.question.mapper.QuestionMapper;
import com.server.capple.domain.question.repository.AdminQuestionRepository;
import com.server.capple.global.exception.RestApiException;
import com.server.capple.global.exception.errorCode.QuestionErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.event.TransactionalEventListener;

@Service
@RequiredArgsConstructor
public class AdminQuestionServiceImpl implements AdminQuestionService {

    private final AdminQuestionRepository adminQuestionRepository;
    private final QuestionService questionService;
    private final QuestionMapper questionMapper;
    private final QuestionCountService questionCountService;
    private final ApplicationEventPublisher applicationEventPublisher;

    @Override
    public QuestionId createQuestion(QuestionCreate request) {
        Question question = adminQuestionRepository.save(questionMapper.toQuestion(request));
        applicationEventPublisher.publishEvent(new QuestionStatusChangedEvent());
        return new QuestionId(question.getId());
    }

    @Override
    @Transactional
    public QuestionId deleteQuestion(Long questionId) {
        Question question = questionService.findQuestion(questionId);

        question.delete();
        applicationEventPublisher.publishEvent(new QuestionStatusChangedEvent());
        return new QuestionId(question.getId());

    }

    @Transactional
    public Question setLiveQuestion() {
        Question newQuestion = adminQuestionRepository.findFirstByQuestionStatusOrderByIdAsc(QuestionStatus.PENDING)
            .orElseThrow(() -> new RestApiException(QuestionErrorCode.QUESTION_PENDING_NOT_FOUND));

        newQuestion.setQuestionStatus(QuestionStatus.LIVE);
        applicationEventPublisher.publishEvent(new QuestionStatusChangedEvent());
        return newQuestion;

    }

    @Transactional
    public Question closeLiveQuestion() {
        Question question = adminQuestionRepository.findFirstByQuestionStatusOrderByIdAsc(QuestionStatus.LIVE)
            .orElseThrow(() -> new RestApiException(QuestionErrorCode.QUESTION_LIVE_NOT_FOUND));
        question.setQuestionStatus(QuestionStatus.OLD);
        applicationEventPublisher.publishEvent(new QuestionStatusChangedEvent());
        return question;
    }

    static class QuestionStatusChangedEvent {
    }

    @TransactionalEventListener(QuestionStatusChangedEvent.class)
    public void handleBoardCreatedEvent() {
        questionCountService.updateLiveOrOldQuestionCount();
    }
}
