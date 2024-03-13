package com.server.capple.domain.question.service;

import com.server.capple.config.security.AuthMember;
import com.server.capple.domain.answer.entity.Answer;
import com.server.capple.domain.answer.repository.AnswerRepository;
import com.server.capple.domain.member.entity.Member;
import com.server.capple.domain.question.dto.response.QuestionResponse.QuestionSummary;
import com.server.capple.domain.question.dto.response.QuestionResponse.QuestionInfos;
import com.server.capple.domain.question.entity.Question;
import com.server.capple.domain.question.mapper.QuestionMapper;
import com.server.capple.domain.question.repository.QuestionRepository;
import com.server.capple.domain.tag.repository.TagRedisRepository;
import com.server.capple.global.exception.RestApiException;
import com.server.capple.global.exception.errorCode.QuestionErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class QuestionServiceImpl implements QuestionService {
    private final QuestionRepository questionRepository;
    private final TagRedisRepository tagRedisRepository;
    private final AnswerRepository answerRepository;

    private final QuestionMapper questionMapper;

    @Override
    public Question findQuestion(Long questionId) {
        return questionRepository.findById(questionId).orElseThrow(()
                -> new RestApiException(QuestionErrorCode.QUESTION_NOT_FOUND));
    }

    @Override
    public QuestionSummary getMainQuestion() {
        return questionMapper.toQuestionSummary(questionRepository.findFirstByOrderByLivedAtDesc().orElseThrow(()
                -> new RestApiException(QuestionErrorCode.QUESTION_NOT_FOUND)));
    }

    @Override
    public QuestionInfos getQuestions(Member member) {
        return questionMapper.toQuestionInfos(questionRepository.findAllByOrderByCreatedAtDesc().orElseThrow(()
                -> new RestApiException(QuestionErrorCode.QUESTION_NOT_FOUND))
                        .stream()
                        .map(question -> questionMapper.toQuestionInfo(question, String.join(" ", tagRedisRepository.getTagsByQuestion(question.getId())), answerRepository.existsByQuestionAndMember(question, member)))
                        .toList());
    }
}
