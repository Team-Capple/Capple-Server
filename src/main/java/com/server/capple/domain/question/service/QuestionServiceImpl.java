package com.server.capple.domain.question.service;

import com.server.capple.domain.answer.repository.AnswerRepository;
import com.server.capple.domain.member.entity.Member;
import com.server.capple.domain.question.dto.response.QuestionResponse;
import com.server.capple.domain.question.dto.response.QuestionResponse.QuestionInfo;
import com.server.capple.domain.question.dto.response.QuestionResponse.QuestionSummary;
import com.server.capple.domain.question.entity.Question;
import com.server.capple.domain.question.mapper.QuestionMapper;
import com.server.capple.domain.question.repository.QuestionHeartRedisRepository;
import com.server.capple.domain.question.repository.QuestionRepository;
import com.server.capple.global.common.SliceResponse;
import com.server.capple.global.exception.RestApiException;
import com.server.capple.global.exception.errorCode.QuestionErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class QuestionServiceImpl implements QuestionService {
    private final QuestionRepository questionRepository;
    private final AnswerRepository answerRepository;
    private final QuestionMapper questionMapper;
    private final QuestionHeartRedisRepository questionHeartRepository;

    @Override
    public Question findQuestion(Long questionId) {
        return questionRepository.findById(questionId).orElseThrow(()
            -> new RestApiException(QuestionErrorCode.QUESTION_NOT_FOUND));
    }

    @Override
    public QuestionSummary getMainQuestion(Member member) {
        Question mainQuestion = questionRepository.findByQuestionStatusIsLiveAndOldOrderByLivedAt()
            .orElseThrow(() -> new RestApiException(QuestionErrorCode.QUESTION_NOT_FOUND));

        boolean isAnswered = answerRepository.existsByQuestionAndMember(mainQuestion, member);

        return questionMapper.toQuestionSummary(mainQuestion, isAnswered);
    }

    @Override
    public SliceResponse<QuestionInfo> getQuestions(Member member, Pageable pageable) {
        return questionMapper.toSliceQuestionInfo(questionRepository.findAllByQuestionStatusIsLiveAndOldOrderByLivedAtDesc(member, pageable));
    }

    @Override
    public QuestionResponse.QuestionToggleHeart toggleQuestionHeart(Member member, Long questionId) {
        Question question = findQuestion(questionId);

        Boolean isLiked = questionHeartRepository.toggleBoardHeart(member.getId(), question.getId());
        return new QuestionResponse.QuestionToggleHeart(questionId, isLiked);
    }
}
