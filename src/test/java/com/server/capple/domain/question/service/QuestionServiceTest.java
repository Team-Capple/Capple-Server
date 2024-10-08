package com.server.capple.domain.question.service;

import com.server.capple.domain.question.dto.response.QuestionResponse.QuestionInfo;
import com.server.capple.domain.question.entity.Question;
import com.server.capple.domain.question.entity.QuestionStatus;
import com.server.capple.support.ServiceTestConfig;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

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
    public void setLiveQuestionTest() {
        //given & when
        Long questionId = adminQuestionService.setLiveQuestion().getId();
        Question question = questionService.findQuestion(questionId);

        //then
        assertEquals(question.getContent(), "가장 좋아하는 음식은 무엇인가요?");
        assertEquals(question.getQuestionStatus(), QuestionStatus.LIVE);
    }

    @Test
    @DisplayName("close Live Question 테스트")
    @Transactional
    public void closeLiveQuestionTest() {
        //given & when
        Long questionId = adminQuestionService.closeLiveQuestion().getId();
        Question question = questionService.findQuestion(questionId);

        //then
        assertEquals(question.getContent(), "아카데미 러너 중 가장 마음에 드는 유형이 있나요?");
        assertEquals(question.getQuestionStatus(), QuestionStatus.OLD);
    }

    @Test
    @DisplayName("get questions 테스트")
    @Transactional
    public void getQuestionsTest() {
        //given & when
        List<QuestionInfo> questionInfos = questionService.getQuestions(member, null, PageRequest.of(0, 1000, Sort.by(Sort.Direction.DESC, "livedAt"))).getContent();

        //then
        assertEquals(questionInfos.size(), 2);
        assertEquals(questionInfos.get(0).getQuestionStatus(), QuestionStatus.LIVE);
        assertEquals(questionInfos.get(0).getIsAnswered(), true);
        assertEquals(questionInfos.get(1).getIsAnswered(), false);
    }

/*
    @Test
    @DisplayName("save popular tags 테스트")
    @Transactional
    public void savePopularTags() {

        //given
        AnswerRequest request = AnswerRequest.builder()
                .answer("인기 태그 테스트")
                .tags(List.of("#태그1", "#태그2", "#바나나와플", "#태그3", "#태그4", "#태그5", "#태그6"))
                .build();
        AnswerRequest request2 = AnswerRequest.builder()
                .answer("나는 와플이랑 바나나를 좋아하는 사람이 좋아")
                .tags(List.of("#바나나", "#와플", "#바나나와플"))
                .build();
        answerService.createAnswer(member, liveQuestion.getId(), request);
        answerService.createAnswer(member, liveQuestion.getId(), request2);

        //when
        adminQuestionService.savePopularTags(liveQuestion.getId());
        List<String> popularTags = Arrays.stream(liveQuestion.getPopularTags().split(" ")).toList();

        //then
        assertEquals(popularTags.get(0), "#바나나와플");
        assertEquals(popularTags.size(), 3);
    }
*/

}
