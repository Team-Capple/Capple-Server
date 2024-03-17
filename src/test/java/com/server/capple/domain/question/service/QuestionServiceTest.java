package com.server.capple.domain.question.service;

import com.server.capple.domain.question.dto.response.QuestionResponse;
import com.server.capple.domain.question.entity.Question;
import com.server.capple.domain.question.entity.QuestionStatus;
import com.server.capple.support.ServiceTestConfig;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DisplayName("Question 서비스의 ")
public class QuestionServiceTest extends ServiceTestConfig {

    @Autowired
    private AdminQuestionService adminQuestionService;
    @Autowired
    private QuestionService questionService;

    @Test
    @DisplayName("set Live Question 테스트")
    @Transactional
    public void setLiveQuestionTest()  {
        //given & when
        QuestionResponse.QuestionId questionId = adminQuestionService.setLiveQuestion();
        Question question = questionService.findQuestion(questionId.getQuestionId());

        //then
        assertEquals(question.getContent(), "가장 좋아하는 음식은 무엇인가요?");
        assertEquals(question.getQuestionStatus(), QuestionStatus.LIVE);
    }

    @Test
    @DisplayName("close Live Question 테스트")
    @Transactional
    public void closeLiveQuestionTest() {
        //given & when
        QuestionResponse.QuestionId questionId = adminQuestionService.closeLiveQuestion();
        Question question = questionService.findQuestion(questionId.getQuestionId());

        //then
        assertEquals(question.getContent(),"아카데미 러너 중 가장 마음에 드는 유형이 있나요?");
        assertEquals(question.getQuestionStatus(), QuestionStatus.OLD);
    }
}
