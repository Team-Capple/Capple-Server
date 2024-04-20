package com.server.capple.support;

import com.server.capple.domain.answer.dto.AnswerRequest;
import com.server.capple.domain.answer.entity.Answer;
import com.server.capple.domain.answer.repository.AnswerRepository;
import com.server.capple.domain.member.entity.Member;
import com.server.capple.domain.member.entity.Role;
import com.server.capple.domain.member.repository.MemberRepository;
import com.server.capple.domain.question.entity.Question;
import com.server.capple.domain.question.entity.QuestionStatus;
import com.server.capple.domain.question.repository.QuestionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDateTime;
import java.util.List;

@SpringBootTest
@ActiveProfiles("test")
public abstract class ServiceTestConfig {
    @Autowired
    protected MemberRepository memberRepository;
    @Autowired
    protected QuestionRepository questionRepository;
    @Autowired
    protected AnswerRepository answerRepository;

    protected Member member;
    protected Question liveQuestion;
    protected Question pendingQuestion;
    protected Question oldQuestion;
    protected Answer answer;

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @BeforeEach
    public void setUp() {
        member = createMember();
        liveQuestion = createLiveQuestion();
        pendingQuestion = createPendingQuestion();
        oldQuestion = createOldQuestion();
        answer = createAnswer();
        redisTemplate.getConnectionFactory().getConnection().flushAll();
    }

    protected Member createMember() {
        return memberRepository.save(
                Member.builder()
                        .nickname("루시")
                        .email("ksm@naver.com")
                        .profileImage("https://owori.s3.ap-northeast-2.amazonaws.com/story/capple_default_image_10635d7a-5f8c-4af2-b062-9a9420634eb3.png")
                        .role(Role.ROLE_ACADEMIER)
                        .email("tnals2384@gmail.com")
                        .sub("2384973284")
                        .build()
        );
    }

    protected Question createLiveQuestion() {
        return questionRepository.save(
                Question.builder()
                        .content("아카데미 러너 중 가장 마음에 드는 유형이 있나요?")
                        .questionStatus(QuestionStatus.LIVE)
                        .livedAt(LocalDateTime.now())
                        .build()
        );
    }

    protected Question createOldQuestion() {
        return questionRepository.save(
                Question.builder()
                        .content("오늘 뭐 먹을 거에요?")
                        .questionStatus(QuestionStatus.OLD)
                        .livedAt(LocalDateTime.of(2024,04,01, 00,00,00))
                        .popularTags("#쌀국수 #와플 #아메리카노")
                        .build()
        );
    }

    protected Question createPendingQuestion() {
        return questionRepository.save(
                Question.builder()
                        .content("가장 좋아하는 음식은 무엇인가요?")
                        .questionStatus(QuestionStatus.PENDING)
                        .build()
        );
    }

    protected Answer createAnswer() {
        return answerRepository.save(Answer.builder()
                .content("나는 무자비한 사람이 좋아")
                .tags("#무자비 #와플 ")
                .question(liveQuestion)
                .member(member)
                .build()
        );
    }

    protected AnswerRequest getAnswerRequest() {
        return AnswerRequest.builder()
                .answer("나는 와플을 좋아하는 사람이 좋아")
                .tags(List.of("#와플유니버시티", "#와플"))
                .build();
    }
}
