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
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AdminQuestionServiceImpl implements AdminQuestionService {

    private final AdminQuestionRepository adminQuestionRepository;
    private final QuestionMapper questionMapper;

    @Override
    public QuestionId createQuestion(QuestionCreate request) {
        Question question = adminQuestionRepository.save(questionMapper.toQuestion(request));

        return new QuestionId(question.getId());
    }

    @Override
    @Transactional
    public QuestionId deleteQuestion(Long questionId) {
        Question question = adminQuestionRepository.findById(questionId).orElseThrow(()
                -> new RestApiException(QuestionErrorCode.QUESTION_NOT_FOUND));

        question.delete();

        return new QuestionId(question.getId());

    }

    @Transactional
    public QuestionId setLiveQuestion() {
        Question newQuestion = adminQuestionRepository.findFirstByQuestionStatusOrderByIdAsc(QuestionStatus.PENDING)
                .orElseThrow(() -> new RestApiException(QuestionErrorCode.QUESTION_PENDING_NOT_FOUND));

        newQuestion.setQuestionStatus(QuestionStatus.LIVE);

        return new QuestionId(newQuestion.getId());

    }

    @Transactional
    public QuestionId closeLiveQuestion() {
        Question question = adminQuestionRepository.findFirstByQuestionStatusOrderByIdAsc(QuestionStatus.LIVE)
                .orElseThrow(() -> new RestApiException(QuestionErrorCode.QUESTION_LIVE_NOT_FOUND));
        question.setQuestionStatus(QuestionStatus.OLD);

        return new QuestionId(question.getId());
    }
}
