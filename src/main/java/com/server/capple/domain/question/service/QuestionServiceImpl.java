package com.server.capple.domain.question.service;

import com.server.capple.domain.answer.repository.AnswerRepository;
import com.server.capple.domain.member.entity.Member;
import com.server.capple.domain.question.dao.QuestionInfoInterface;
import com.server.capple.domain.question.dto.response.QuestionResponse;
import com.server.capple.domain.question.dto.response.QuestionResponse.QuestionInfos;
import com.server.capple.domain.question.dto.response.QuestionResponse.QuestionSummary;
import com.server.capple.domain.question.entity.Question;
import com.server.capple.domain.question.entity.QuestionStatus;
import com.server.capple.domain.question.mapper.QuestionMapper;
import com.server.capple.domain.question.repository.QuestionRepository;
import com.server.capple.domain.tag.repository.TagRedisRepository;
import com.server.capple.domain.tag.service.TagService;
import com.server.capple.global.exception.RestApiException;
import com.server.capple.global.exception.errorCode.QuestionErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.server.capple.domain.question.dto.response.QuestionResponse.*;
import static com.server.capple.domain.question.entity.QuestionStatus.*;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class QuestionServiceImpl implements QuestionService {
    private final QuestionRepository questionRepository;
    private final TagService tagService;
    private final AnswerRepository answerRepository;
    private final QuestionMapper questionMapper;

    @Override
    public Question findQuestion(Long questionId) {
        return questionRepository.findById(questionId).orElseThrow(()
                -> new RestApiException(QuestionErrorCode.QUESTION_NOT_FOUND));
    }

    @Override
    public QuestionSummary getMainQuestion(Member member) {
        Question mainQuestion = questionRepository.findByQuestionStatusIsLiveAndOldOrderByLivedAt().orElseThrow(()
                -> new RestApiException(QuestionErrorCode.QUESTION_NOT_FOUND));

        boolean isAnswered = answerRepository.existsByQuestionAndMember(mainQuestion, member);

        return questionMapper.toQuestionSummary(mainQuestion, isAnswered);
    }

    @Override
    public QuestionInfos getQuestions(Member member) {
        List<QuestionInfoInterface> questions = questionRepository.findAllByQuestionStatusIsLiveAndOldOrderByLivedAtDesc(member);

        return questionMapper.toQuestionInfos(questions
                .stream()
                .map(questionInfo -> {
                    Question question = questionInfo.getQuestion();

                    String tags = question.getQuestionStatus().equals(LIVE) ?
                            String.join(" ", tagService.getTagsByQuestion(question.getId(),3).getTags()) :
                            question.getPopularTags().trim();

                    return questionMapper.toQuestionInfo(question, tags, questionInfo.getIsAnsweredByMember());
                }).toList());
    }
}
