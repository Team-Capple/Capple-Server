package com.server.capple.domain.question.service;

import com.server.capple.domain.question.dto.request.QuestionRequest.QuestionCreate;
import com.server.capple.domain.question.dto.response.QuestionResponse;
import com.server.capple.domain.question.dto.response.QuestionResponse.QuestionId;
import com.server.capple.domain.question.entity.Question;
import com.server.capple.domain.question.mapper.QuestionMapper;
import com.server.capple.domain.question.repository.AdminQuestionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AdminQuestionServiceImpl implements AdminQuestionService {

    private final AdminQuestionRepository adminQuestionRepository;
    private final QuestionMapper questionMapper;
    @Override
    public QuestionId createQuestion(QuestionCreate request) {
        Question question = adminQuestionRepository.save(questionMapper.toQuestion(request));

        return questionMapper.toQuestionId(question);
    }
}
