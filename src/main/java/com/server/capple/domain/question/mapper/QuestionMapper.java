package com.server.capple.domain.question.mapper;

import com.server.capple.domain.question.dto.request.QuestionRequest.QuestionCreate;
import com.server.capple.domain.question.dto.response.QuestionResponse.QuestionInfo;
import com.server.capple.domain.question.dto.response.QuestionResponse.QuestionInfos;
import com.server.capple.domain.question.dto.response.QuestionResponse.QuestionSummary;
import com.server.capple.domain.question.entity.Question;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class QuestionMapper {

    public Question toQuestion(QuestionCreate request) {
        return Question.builder()
                .questionStatus(request.getQuestionStatus())
                .content(request.getContent())
                .commentCount(0)
                .build();
    }


    public QuestionSummary toQuestionSummary(Question question, boolean isAnswered, Integer likeCount) {
        return QuestionSummary.builder()
                .questionId(question.getId())
                .questionStatus(question.getQuestionStatus())
                .content(question.getContent())
                .isAnswered(isAnswered)
                .likeCount(likeCount)
                .commentCount(question.getCommentCount())
                .build();
    }

    public QuestionInfo toQuestionInfo(Question question, boolean isAnswered, Integer likeCount) {
        return QuestionInfo.builder()
                .questionId(question.getId())
                .questionStatus(question.getQuestionStatus())
                .livedAt(question.getLivedAt())
                .content(question.getContent())
                .likeCount(likeCount)
                .commentCount(question.getCommentCount())
                .isAnswered(isAnswered)
                .build();
    }

    public QuestionInfos toQuestionInfos(List<QuestionInfo> questionInfoList) {
        return QuestionInfos.builder()
                .questionInfos(questionInfoList)
                .build();
    }
}
