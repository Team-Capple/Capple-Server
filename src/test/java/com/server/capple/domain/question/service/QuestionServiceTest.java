package com.server.capple.domain.question.service;

import com.server.capple.domain.answer.entity.Answer;
import com.server.capple.domain.answer.service.AnswerCountService;
import com.server.capple.domain.member.entity.Member;
import com.server.capple.domain.member.entity.Role;
import com.server.capple.domain.question.dto.response.QuestionResponse.QuestionInfo;
import com.server.capple.domain.question.entity.Question;
import com.server.capple.domain.question.entity.QuestionStatus;
import com.server.capple.global.common.SliceResponse;
import com.server.capple.support.ServiceTestConfig;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;
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

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;
    @Autowired
    private AnswerCountService answerCountService;

    @DisplayName("사용자가 답변한 질문을 조회한다.")
    @Transactional
    @TestFactory
    public Collection<DynamicTest> getAnsweredQuestions() {
        // given
        LocalDateTime thresholdDateTime = LocalDateTime.now().minusYears(20);
        int pageSize = 3;
        Member member1 = createQuestionMember("사용자1");
        memberRepository.save(member1);
        List<Question> questions = createQuestions(thresholdDateTime);
        List<Answer> answers = List.of(
            createAnswer(member1, questions.get(0)),
            createAnswer(member1, questions.get(1)),
            createAnswer(member1, questions.get(2)),
            createAnswer(member1, questions.get(5)),
            createAnswer(member1, questions.get(6)));
        answerRepository.saveAll(answers);

        return List.of(
            DynamicTest.dynamicTest("첫 페이지 조회", () -> {
                // when
                SliceResponse<QuestionInfo> response = questionService.getAnsweredQuestions(
                    member1,
                    thresholdDateTime,
                    PageRequest.of(0, pageSize, Sort.by(Sort.Direction.DESC, "livedAt"))
                );

                // then
                assertThat(response.getTotal()).isEqualTo(answers.size());
                assertThat(response.getNumberOfElements()).isEqualTo(pageSize);
                assertThat(response.getSize()).isEqualTo(pageSize);
                assertThat(response.getThreshold()).isEqualTo(questions.get(2).getLivedAt().toString());
                assertThat(response.isHasNext()).isEqualTo(true);
                assertThat(response.getContent()).hasSize(3)
                    .extracting("content", "questionStatus", "livedAt", "isAnswered")
                    .containsExactlyInAnyOrder(
                        tuple(questions.get(6).getContent(), questions.get(6).getQuestionStatus(), questions.get(6).getLivedAt(), true),
                        tuple(questions.get(5).getContent(), questions.get(5).getQuestionStatus(), questions.get(5).getLivedAt(), true),
                        tuple(questions.get(2).getContent(), questions.get(2).getQuestionStatus(), questions.get(2).getLivedAt(), true)
                    );
            }),
            DynamicTest.dynamicTest("마지막 페이지 조회", () -> {
                // when
                SliceResponse<QuestionInfo> response = questionService.getAnsweredQuestions(
                    member1,
                    questions.get(2).getLivedAt(),
                    PageRequest.of(0, pageSize, Sort.by(Sort.Direction.DESC, "livedAt"))
                );

                // then
                assertThat(response.getTotal()).isEqualTo(answers.size());
                assertThat(response.getNumberOfElements()).isEqualTo(2);
                assertThat(response.getSize()).isEqualTo(pageSize);
                assertThat(response.getThreshold()).isEqualTo(questions.get(0).getLivedAt().toString());
                assertThat(response.isHasNext()).isEqualTo(false);
                assertThat(response.getContent()).hasSize(2)
                    .extracting("content", "questionStatus", "livedAt", "isAnswered")
                    .containsExactlyInAnyOrder(
                        tuple(questions.get(1).getContent(), questions.get(1).getQuestionStatus(), questions.get(1).getLivedAt(), true),
                        tuple(questions.get(0).getContent(), questions.get(0).getQuestionStatus(), questions.get(0).getLivedAt(), true)
                    );
                answerCountService.expireMembersAnsweredCount(member1);
            })
        );
    }

    @DisplayName("사용자가 답변하지 않은 질문을 조회한다.")
    @Transactional
    @TestFactory
    public Collection<DynamicTest> getNotAnsweredQuestions() {
        // given
        LocalDateTime thresholdDateTime = LocalDateTime.now().minusYears(20);
        int pageSize = 3;
        Member member1 = createQuestionMember("사용자1");
        memberRepository.save(member1);
        List<Question> questions = createQuestions(thresholdDateTime);
        List<Answer> answers = List.of(
            createAnswer(member1, questions.get(3)),
            createAnswer(member1, questions.get(0)),
            createAnswer(member1, questions.get(5)));
        answerRepository.saveAll(answers);

        return List.of(
            DynamicTest.dynamicTest("첫 페이지 조회", () -> {
                // when
                SliceResponse<QuestionInfo> response = questionService.getNotAnsweredQuestions(
                    member1,
                    thresholdDateTime,
                    PageRequest.of(0, pageSize, Sort.by(Sort.Direction.DESC, "livedAt"))
                );

                // then
//                assertThat(response.getTotal()).isEqualTo(totalQuestionCnt - answers.size()); // 질문이 테스트 케이스 밖에서부터 생성되어 있어 테스트 불가능
                assertThat(response.getNumberOfElements()).isEqualTo(pageSize);
                assertThat(response.getSize()).isEqualTo(pageSize);
                assertThat(response.getThreshold()).isEqualTo(questions.get(2).getLivedAt().toString());
                assertThat(response.isHasNext()).isEqualTo(true);
                assertThat(response.getContent()).hasSize(3)
                    .extracting("content", "questionStatus", "livedAt", "isAnswered")
                    .containsExactlyInAnyOrder(
                        tuple(questions.get(6).getContent(), questions.get(6).getQuestionStatus(), questions.get(6).getLivedAt(), false),
                        tuple(questions.get(4).getContent(), questions.get(4).getQuestionStatus(), questions.get(4).getLivedAt(), false),
                        tuple(questions.get(2).getContent(), questions.get(2).getQuestionStatus(), questions.get(2).getLivedAt(), false)
                    );
            }),
            DynamicTest.dynamicTest("마지막 페이지 조회", () -> {
                // when
                SliceResponse<QuestionInfo> response = questionService.getNotAnsweredQuestions(
                    member1,
                    questions.get(2).getLivedAt(),
                    PageRequest.of(0, pageSize, Sort.by(Sort.Direction.DESC, "livedAt"))
                );

                // then
//                assertThat(response.getTotal()).isEqualTo(totalQuestionCnt - answers.size()); // 질문이 테스트 케이스 밖에서부터 생성되어 있어 테스트 불가능
                assertThat(response.getNumberOfElements()).isEqualTo(1);
                assertThat(response.getSize()).isEqualTo(pageSize);
                assertThat(response.getThreshold()).isEqualTo(questions.get(1).getLivedAt().toString());
                assertThat(response.isHasNext()).isEqualTo(false);
                assertThat(response.getContent()).hasSize(1)
                    .extracting("content", "questionStatus", "livedAt", "isAnswered")
                    .containsExactlyInAnyOrder(
                        tuple(questions.get(1).getContent(), questions.get(1).getQuestionStatus(), questions.get(1).getLivedAt(), false)
                    );

                redisTemplate.delete("questionCount::SimpleKey []");
                answerCountService.expireMembersAnsweredCount(member1);
            })
        );
    }

    private Member createQuestionMember(String userName) {
        return Member.builder()
            .role(Role.ROLE_ACADEMIER)
            .nickname(userName)
            .email("")
            .sub("")
            .build();
    }

    private List<Question> createQuestions(LocalDateTime thresholdDateTime) {
        List<Question> questions = new ArrayList<>();
        questions.add(Question.builder().content("질문1").livedAt(thresholdDateTime.minusDays(7)).questionStatus(QuestionStatus.OLD).build());
        questions.add(Question.builder().content("질문2").livedAt(thresholdDateTime.minusDays(6)).questionStatus(QuestionStatus.OLD).build());
        questions.add(Question.builder().content("질문3").livedAt(thresholdDateTime.minusDays(5)).questionStatus(QuestionStatus.OLD).build());
        questions.add(Question.builder().content("질문4").livedAt(thresholdDateTime.minusDays(4)).questionStatus(QuestionStatus.OLD).build());
        questions.add(Question.builder().content("질문5").livedAt(thresholdDateTime.minusDays(3)).questionStatus(QuestionStatus.OLD).build());
        questions.add(Question.builder().content("질문6").livedAt(thresholdDateTime.minusDays(2)).questionStatus(QuestionStatus.OLD).build());
        questions.add(Question.builder().content("질문7").livedAt(thresholdDateTime.minusHours(1)).questionStatus(QuestionStatus.LIVE).build());
        questions.add(Question.builder().content("질문8").questionStatus(QuestionStatus.PENDING).build());
        questionRepository.saveAll(questions);
        return questions;
    }

    private Answer createAnswer(Member member, Question question) {
        return Answer.builder()
            .member(member)
            .question(question)
            .content("content")
            .build();
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
