package com.server.capple.domain.question.service;

import com.server.capple.domain.answer.repository.AnswerRepository;
import com.server.capple.domain.answerComment.repository.AnswerCommentHeartRedisRepository;
import com.server.capple.domain.board.dto.BoardResponse;
import com.server.capple.domain.board.entity.Board;
import com.server.capple.domain.member.entity.Member;
import com.server.capple.domain.question.dao.QuestionInfoInterface;
import com.server.capple.domain.question.dto.response.QuestionResponse;
import com.server.capple.domain.question.dto.response.QuestionResponse.QuestionInfos;
import com.server.capple.domain.question.dto.response.QuestionResponse.QuestionSummary;
import com.server.capple.domain.question.entity.Question;
import com.server.capple.domain.question.mapper.QuestionMapper;
import com.server.capple.domain.question.repository.QuestionHeartRedisRepository;
import com.server.capple.domain.question.repository.QuestionRepository;
import com.server.capple.global.exception.RestApiException;
import com.server.capple.global.exception.errorCode.QuestionErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class QuestionServiceImpl implements QuestionService {
    private final QuestionRepository questionRepository;
    private final AnswerRepository answerRepository;
    private final QuestionMapper questionMapper;
    private final QuestionHeartRedisRepository questionHeartRepository;
    private final AnswerCommentHeartRedisRepository answerCommentHeartRepository;

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

        return questionMapper.toQuestionSummary(mainQuestion, isAnswered/*, questionHeartRepository.getQuestionHeartsCount(mainQuestion.getId())*/);
    }

    @Override
    public QuestionInfos getQuestions(Member member) {
        List<QuestionInfoInterface> questions = questionRepository.findAllByQuestionStatusIsLiveAndOldOrderByLivedAtDesc(member);

        return questionMapper.toQuestionInfos(questions.stream()
                .map(questionInfo ->
                        questionMapper.toQuestionInfo(questionInfo.getQuestion(),
                                questionInfo.getIsAnsweredByMember()/*,
                                questionHeartRepository.getQuestionHeartsCount(questionInfo.getQuestion().getId())*/)
                ).toList());
    }

    @Override
    public QuestionResponse.QuestionToggleHeart toggleQuestionHeart(Member member, Long questionId) {
        Question question = findQuestion(questionId);

        Boolean isLiked = questionHeartRepository.toggleBoardHeart(member.getId(), question.getId());
        return new QuestionResponse.QuestionToggleHeart(questionId, isLiked);
    }
}
