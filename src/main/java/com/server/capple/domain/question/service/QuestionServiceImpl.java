package com.server.capple.domain.question.service;
import com.server.capple.domain.question.dto.response.QuestionResponse.QuestionSummary;
import com.server.capple.domain.question.entity.Question;
import com.server.capple.domain.question.entity.QuestionStatus;
import com.server.capple.domain.question.mapper.QuestionMapper;
import com.server.capple.domain.question.repository.QuestionRepository;
import com.server.capple.global.exception.RestApiException;
import com.server.capple.global.exception.errorCode.QuestionErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class QuestionServiceImpl implements QuestionService {
    private final QuestionRepository questionRepository;
    private final QuestionMapper questionMapper;

    public Question findQuestion(Long questionId) {
        return questionRepository.findById(questionId).orElseThrow(()
                -> new RestApiException(QuestionErrorCode.QUESTION_NOT_FOUND));
    }

    @Override
    public QuestionSummary getQuestionSummary(QuestionStatus questionStatus) {
        System.out.println("1");
        System.out.println(questionStatus.toString());
        System.out.println("2");
        System.out.println(questionStatus.name());
        if (questionStatus == QuestionStatus.OLD) {
            return questionMapper.toQuestionSummary(questionRepository.findByLastOldQuestion(questionStatus).orElseThrow(()
                    -> new RestApiException(QuestionErrorCode.QUESTION_NOT_FOUND)));
        }
//        else if (questionStatus == QuestionStatus.LIVE) {
//            return questionMapper.toQuestionSummary(questionRepository.findByLiveQuestion(questionStatus.name()).orElseThrow(()
//            -> new RestApiException(QuestionErrorCode.QUESTION_NOT_FOUND)));
//        }

        return null;
    }
}
