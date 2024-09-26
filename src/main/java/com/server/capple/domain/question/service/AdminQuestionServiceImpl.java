package com.server.capple.domain.question.service;

import com.opencsv.CSVReader;
import com.opencsv.bean.CsvToBeanBuilder;
import com.server.capple.domain.question.dto.internal.QuestionDto.QuestionInsertDto;
import com.server.capple.domain.question.dto.request.QuestionRequest.QuestionCreate;
import com.server.capple.domain.question.dto.response.QuestionResponse.QuestionId;
import com.server.capple.domain.question.entity.Question;
import com.server.capple.domain.question.entity.QuestionStatus;
import com.server.capple.domain.question.mapper.QuestionMapper;
import com.server.capple.domain.question.repository.QuestionRepository;
import com.server.capple.global.exception.RestApiException;
import com.server.capple.global.exception.errorCode.QuestionErrorCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.StringReader;
import java.util.Comparator;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class AdminQuestionServiceImpl implements AdminQuestionService {

    private final QuestionRepository questionRepository;
    private final QuestionService questionService;
    private final QuestionMapper questionMapper;

    @Override
    public QuestionId createQuestion(QuestionCreate request) {
        Question question = questionRepository.save(questionMapper.toQuestion(request));

        return new QuestionId(question.getId());
    }

    @Override
    @Transactional
    public QuestionId deleteQuestion(Long questionId) {
        Question question = questionService.findQuestion(questionId);

        question.delete();

        return new QuestionId(question.getId());

    }

    @Transactional
    public Question setLiveQuestion() {
        Question newQuestion = questionRepository.findFirstByQuestionStatusOrderByIdAsc(QuestionStatus.PENDING)
            .orElseThrow(() -> new RestApiException(QuestionErrorCode.QUESTION_PENDING_NOT_FOUND));

        newQuestion.setQuestionStatus(QuestionStatus.LIVE);

        return newQuestion;

    }

    @Transactional
    public Question closeLiveQuestion() {
        Question question = questionRepository.findFirstByQuestionStatusOrderByIdAsc(QuestionStatus.LIVE)
            .orElseThrow(() -> new RestApiException(QuestionErrorCode.QUESTION_LIVE_NOT_FOUND));
        question.setQuestionStatus(QuestionStatus.OLD);

        return question;
    }

    @Override
    public Long uploadQuestionByCsv(String text) {
        List<QuestionInsertDto> questionInsertDtoList = new CsvToBeanBuilder(new CSVReader(new StringReader(text)))
            .withType(QuestionInsertDto.class)
            .build()
            .parse();
        questionInsertDtoList.sort(Comparator.comparing(QuestionInsertDto::getQuestionId));
        List<Question> questions = questionInsertDtoList.stream()
            .map(questionMapper::toQuestionInsertDto)
            .toList();
        Long savedQuestionCount = (long) questionRepository.saveAll(questions).size();
        log.info("{} 개의 질문이 새롭게 추가되었습니다.", savedQuestionCount);
        return savedQuestionCount;
    }
}
